pipeline {
    agent any
    
    tools {
        // Qué herramientas queremos utilizar
        maven "Maven3"
        jdk "JDK17"
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
                bat "mvn clean package -DskipTests"
                // sh "mvn -Dmaven.test.failure.ignore=true clean package" // Unix
            }
        }
        
        stage('test maven'){
            steps{
                bat "mvn test"
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
                    nexusUrl: 'localhost:8081',
                    groupId: 'QA',
                    version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                    repository: 'nombre repositorio nexus', // Se debe cambiar
                    credentialsId: 'NexusLogin', //nombre de credencial para nexus
                    artifacts: [
                        [artifactId: 'devops',
                        classifier: '',
                        file: 'target/devops-0.0.1-SNAPSHOT.jar',
                        type: 'jar']
                    ]
                )
            }
        }
        
        stage('Sonar Analysys'){
            steps{
                echo 'Sonar Analysis'
                withSonarQubeEnv('Sonarqube'){
                    bat "mvn clean package sonar:sonar"
                }
            }
        }
        
        stage('Upload Artifact'){
            steps{
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: 'localhost:8081',
                    groupId: 'QA',
                    version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                    repository: 'DevOpsRepository',
                    credentialsId: 'nexus_admin',
                    artifacts: [
                        //[artifactId: 'webApp',
                        // classifier: '',
                        // file: 'web/target/time-tracker-web-0.5.0-SNAPSHOT.war',
                        // type: 'war'],
                        [artifactId: 'coreApp',
                         classifier: '',
                         file: 'target/devops-0.0.1-SNAPSHOT.jar',
                         type: 'jar']
                        ]
                    )
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
