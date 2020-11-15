pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew assemble'
                echo 'Build'
            }
        }
        stage('Test'){
            steps {
                sh './gradlew test'
                echo 'Test'
            }
        }
        stage('Dockerize'){
            steps {
                sh "docker build -t 674186390846.dkr.ecr.eu-west-1.amazonaws.com/spamdit ."
            }
        }
        stage('Push image to ECR'){
            steps {
                sh '$(aws ecr get-login --registry-ids 674186390846 --no-include-email --region eu-west-1)'
                sh "docker push 674186390846.dkr.ecr.eu-west-1.amazonaws.com/spamdit"
            }
        }
        stage('Update ECR service'){
            steps {
                sh 'aws ecs update-service --cluster spamdit-cluster --service Spamdit-service --force-new-deployment --region eu-west-1'
            }
        }
    }
    post {
        always {
            sh "docker system prune -af"
        }
    }
}