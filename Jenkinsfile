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
