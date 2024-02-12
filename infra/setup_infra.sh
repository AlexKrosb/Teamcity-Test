echo "Current directory: $(pwd)"
cd ..
echo "Current directory: $(pwd)"
teamcity_tests_directory=$(pwd)
workdir="teamcity_tests_infrastructure"
teamcity_server_workdir="teamcity_server"
teamcity_agent_workdir="teamcity_agent"
selenoid_workdir="selenoid"
teamcity_server_container_name="teamcity_server_instance"
teamcity_agent_container_name="teamcity_agent_instance"
selenoid_container_name="selenoid_instance"
selenoid_ui_container_name="selenoid_ui_instance"

workdir_names=($teamcity_server_workdir $teamcity_agent_workdir $selenoid_workdir)
container_names=($teamcity_server_container_name $teamcity_agent_container_name $selenoid_container_name $selenoid_ui_container_name)

#################################################
echo "Request IP"
export ips=$(powershell.exe 'ipconfig | Select-String -Pattern "IPv4" | ForEach-Object { $_.ToString().Split()[-1] } | Where-Object { $_ -ne "127.0.0.1" }')
export ip=$(echo "$ips" | awk 'NR==2 {print $NF}')
echo "Current IP: $ip"

################################
echo "Delete previous run data"
rm -rf $workdir
mkdir $workdir
echo "Current directory: $(pwd)"

cd $workdir

for dir in "${workdir_names[@]}"; do
  mkdir $dir
done

for container in "${container_names[@]}"; do
  docker stop $container
  docker rm $container
done

################################
echo "Start teamcity server"
echo "Current directory: $(pwd)"

mkdir $teamcity_server_workdir
cd $teamcity_server_workdir

current=$(powershell.exe '$PWD -replace "\\", "/" -replace "C", "c"')

docker run -d --name $teamcity_server_container_name  \
    -v $current/logs:/opt/teamcity/logs  \
    -p 8111:8111 \
    jetbrains/teamcity-server

echo "Teamcity Server is running..."

################################
echo "Start selenoid"
echo "Current directory: $(pwd)"

cd ..

mkdir $selenoid_workdir
cd $selenoid_workdir
mkdir config

cd ..
cd ..
teamcity_tests_directory=$(pwd)
cd $teamcity_tests_directory/teamcity_tests_infrastructure/selenoid/
cp $teamcity_tests_directory/infra/browsers.json config/
selenoid_container_name="selenoid_instance"
current=$(powershell.exe '$PWD -replace "\\", "/" -replace "C", "c"')
docker run -d                                   \
            --name $selenoid_container_name                                 \
            -p 4444:4444                                    \
            -v //var/run/docker.sock:/var/run/docker.sock    \
            -v $current/config/:/etc/selenoid/:ro              \
    aerokube/selenoid:latest-release

image_names=($(awk -F'"' '/"image": "/{print $4}' "$(pwd)/config/browsers.json"))

echo "Pull all browser images: $image_names"

for image in "${image_names[@]}"; do
  docker pull $image
done

################################
echo "Start selenoid-ui"
cd ..
selenoid_ui_container_name="selenoid_ui_instance"
docker run -d --name $selenoid_ui_container_name \
    -p 80:8080 aerokube/selenoid-ui:latest-release --selenoid-uri "http://$ip:4444"

################################
echo "Setup teamcity server"

cd ..

echo "Current directory: $(pwd)"

mvn clean test -Dtest=SetupTest#startUpTest

################################
echo "Parse superuser token"

superuser_token=$(grep -o 'Super user authentication token: [0-9]*' $teamcity_tests_directory/$workdir/$teamcity_server_workdir/logs/teamcity-server.log | awk '{print $NF}')
echo "Super user token: $superuser_token"

################################
echo "Run system tests"

config=$teamcity_tests_directory/src/main/resources/config.properties

echo "host=$ip:8111" > $config
echo "superUserToken=$superuser_token" >> $config
echo "remote=http://$ip:4444/wd/hub" >> $config
echo "browser=firefox" >> $config
cat $config

echo "Current directory: $(pwd)"

echo "Run API tests"
mvn test -DsuiteXmlFile=testng-suites/api-suite.xml

echo "Run UI tests"
mvn test -DsuiteXmlFile=testng-suites/ui-suite.xml

