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
    }
    
    stages {
        stage('Hello'){
            steps{
                echo "DB Engine is: ${DB_ENGINE}"
                echo "DISABLE_AUTH is: ${DISABLE_AUTH}"
                echo "Build ${env.BUILD_ID} on ${env.JENKINS_URL}"
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
        
        stage('Upload Artifact'){
            steps{
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: 'nexus.devops-elgrupo.keberlabs.com',
                    groupId: 'QA',
                    version: "${env.BUILD_ID}",
                    repository: 'proyecto-devops', 
                    credentialsId: 'nexus_admin', //nombre de credencial para nexus
                    artifacts: [
                        [artifactId: 'devops',
                        classifier: '',
                        file: 'target/simple-springmvc-docker.jar',
                        type: 'jar']
                    ]
                )        
            }
        }        

        /*
        stage('Sonar Analysis'){
            steps{
                echo 'Sonar Analysis'
                withSonarQubeEnv('sonar'){
                    //bat "mvn clean package sonar:sonar"
                    sh "mvn clean package sonar:sonar"
                }
            }
        }
        */

        stage('Build Docker Image'){
            steps{
                def dockerfile = '''
                    FROM openjdk:17-jdk-slim-bullseye
                    RUN addgroup -system devopsc && useradd -G devopsc javams
                    USER javams:devopsc
                    ENV JAVA_OPTS=""                    
                    COPY devops-${env.BUILD_ID}.jar /app.jar
                    VOLUME /tmp
                    EXPOSE 9090
                    ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
                '''

                writeFile file: 'Dockerfile', text: dockerfile
                sh "docker build -t examenfinal:${DOCKER_IMAGE_TAG} ."
            }
        }
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
