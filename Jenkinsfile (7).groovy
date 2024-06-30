pipeline {
    agent any

    environment {
        DOCKER_HUB_REPO = 'najlaejl2/skills_factory_app'
        APP_REPO_URL = 'https://github.com/NajlaeJellouli/app.git'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: APP_REPO_URL
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${env.DOCKER_HUB_REPO}:app-${env.BUILD_ID}", '-f Dockerfile .')
                    dockerImage.push()
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    def imageTag = "app-${env.BUILD_ID}"
                    def podName = "app-pod"
                    def svcName = "app-svc"
                    def port = 8080
                    def targetPort = 8080

                    bat "kubectl run ${podName} --image=${env.DOCKER_HUB_REPO}:${imageTag}"
                    bat "kubectl expose pod ${podName} --type=NodePort --port=${port} --target-port=${targetPort} --name=${svcName}"
                }
            }
        }
    }
}
