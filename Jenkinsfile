def COLOR_MAP = ['SUCCESS': 'good', 'FAILURE': 'danger', 'UNSTABLE': 'danger', 'ABORTED': 'danger']

pipeline {
    agent any
    
    tools {
        // Qué herramientas queremos utilizar
        maven "mvn3"
        jdk "java17"
    }
    
    environment {
        NEXUS_VERSION = "nexus3"
        DISABLE_AUTH = 'true'
        DB_ENGINE = 'sqlite'
        NEXUS_ARTIFACT_ID = 'targetApp'
        NEXUS_URL = 'nexus.devops-elgrupo.keberlabs.com'
        NEXUS_REPOSITORY = 'proyecto-devops'
        NEXUS_GROUP_ID = 'QA'
        NEXUS_ARTIFACT_URL = "https://${NEXUS_URL}/repository/${NEXUS_REPOSITORY}/${NEXUS_GROUP_ID}/${NEXUS_ARTIFACT_ID}/${env.BUILD_ID}/${NEXUS_ARTIFACT_ID}-${env.BUILD_ID}.jar"
    }
    
    stages {
        stage('Hello'){
            steps{
                echo "DB Engine is: ${DB_ENGINE}"
                echo "DISABLE_AUTH is: ${DISABLE_AUTH}"
                echo "Build ${env.BUILD_ID} on ${env.JENKINS_URL}"

                slackSend channel: '#proyecto-final',
                color: COLOR_MAP[currentBuild.currentResult],
                message:"*${currentBuild.currentResult}: Exiting News! Starting Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n"
            }
        }   
        
        stage('Git polling'){
            steps{
                git branch: 'main', credentialsId: 'github-keber', url: 'git@github.com:keber/ProjectDevops.git'
            }
        }
        
        stage('Build con maven'){
            steps{
                // bat "mvn clean package -DskipTests"
                sh "mvn -Dmaven.test.failure.ignore=true clean package" // Unix
            }
        }
        
        stage('test maven'){
            steps{
                //bat "mvn test"
                sh "mvn test"
            }
            
            post{
                success{
                    echo 'Archivar artefactos'
                    archiveArtifacts 'target/*.jar'
                    //archiveArtifacts 'core/target/*.jar'
                    //archiveArtifacts 'web/target/*.war'
                }
            }
        }

        stage('Sonar Analysis'){
            steps{
                echo 'Sonar Analysis'
                withSonarQubeEnv('sonar'){
                    //bat "mvn clean package sonar:sonar"
                    sh "mvn clean package sonar:sonar"
                }
            }
        }
        
        stage('Upload Artifact'){
            steps{
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: "${NEXUS_URL}",
                    groupId: "${NEXUS_GROUP_ID}",
                    version: "${env.BUILD_ID}",
                    repository: "${NEXUS_REPOSITORY}", 
                    credentialsId: 'nexus_admin', //nombre de credencial para nexus
                    artifacts: [
                        [artifactId: "${NEXUS_ARTIFACT_ID}",
                        classifier: '',
                        file: 'target/simple-springmvc-docker.jar',
                        type: 'jar']
                    ]
                )        
            }
            post{
                success{
                    slackSend channel: '#proyecto-final',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message:"*${currentBuild.currentResult}: Great news! Artifact uploaded to nexus ${NEXUS_ARTIFACT_URL}. Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n"
                }
            }
        }
        

        stage('Build Docker Image'){
            steps{

              script {
                dockerfile = """
                    FROM openjdk:17-jdk-slim-bullseye
                    RUN addgroup -system devopsc && useradd -G devopsc javams
                    USER javams:devopsc
                    ADD ${NEXUS_ARTIFACT_URL} app.jar                    
                    VOLUME /tmp
                    EXPOSE 9090
                    ENTRYPOINT [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
                """
              }
              
              writeFile file: 'Dockerfile', text: dockerfile
              //sh "docker build -t examenfinal:${DOCKER_IMAGE_TAG} ."
              sh "docker build -t keberflores/examenfinal ."
            }
            post{
                success{
                    slackSend channel: '#proyecto-final',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message:"*${currentBuild.currentResult}: Awesome! Docker image built. Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n"
                }
            }
        }

        stage('Push Docker Image'){
            steps{
                sh "docker push keberflores/examenfinal:${env.BUILD_ID}"
                //sh "docker push keberflores/examenfinal:latest"
            }
            post{
                success{
                    slackSend channel: '#proyecto-final',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message:"*${currentBuild.currentResult}: Hello World! Docker image online at https://hub.docker.com/repository/docker/keberflores/targetapp/general . Ready to go live? Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n"
                }
            }
        }

        /* WIP
        stage('Deploy App'){
            steps{
                sh "docker pull keberflores/examenfinal:latest"
                sh "docker run -p keberflores/examenfinal:latest
            }
            post{
                success{
                    slackSend channel: '#proyecto-final',
                    color: COLOR_MAP[currentBuild.currentResult],
                    message:"*${currentBuild.currentResult}: Hello World! Docker image online. Ready to go live? Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n"
                }
            }
        }
        */
    }
    
    post {
        always{
            echo "Slack Notifications"
            slackSend channel: '#proyecto-final',
            color: COLOR_MAP[currentBuild.currentResult],
            message:"*${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More Info at: ${env.BUILD_URL}"
        }
    }
}
