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
                    nexusUrl: 'http://devops-elgrupo.keberlabs.com:8081/',
                    groupId: 'QA',
                    version: "${env.BUILD_ID}-${env.BUILD_TIMESTAMP}",
                    repository: 'proyecto-devops', 
                    credentialsId: 'nexus_admin', //nombre de credencial para nexus
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
                withSonarQubeEnv('sonar'){
                    //bat "mvn clean package sonar:sonar"
                    sh "mvn clean package sonar:sonar"
                }
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
