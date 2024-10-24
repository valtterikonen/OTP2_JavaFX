pipeline {
    agent any
    environment {
        DOCKERHUB_CREDENTIALS = credentials('8169bb21-1a4c-4562-9338-13d446396218')
        DOCKER_IMAGE = 'valtterikonen/otp2_vk1_localdemo1:latest'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/valtterikonen/LocalizedGreeting.git' 
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Docker Build') {
            steps {
                script {
                    docker.build("${env.DOCKER_IMAGE}")
                }
            }
        }
        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'DOCKERHUB_CREDENTIALS') {
                        docker.image("${env.DOCKER_IMAGE}").push()
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    sh 'curl -X POST https://labs.play-with-docker.com/instances -d "image=${env.DOCKER_IMAGE}"'
                }
            }
        }
    }
}
