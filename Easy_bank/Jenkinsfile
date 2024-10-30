pipeline {
    agent any
    tools {
        maven 'Maven3.9.9'  // Use the correct Maven tool name
        jdk 'JDK 21'        // Use the correct JDK tool name
    }
    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'final', url: 'https://github.com/Mikkomannine/EasyBank.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
        stage('Run Unit Tests') {
            steps {
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'  // Capture test reports
                }
            }
        }
        stage('Code Coverage Report') {
            steps {
                bat 'mvn jacoco:report'
            }
            post {
                always {
                    jacoco execPattern: 'target/jacoco.exec'
                }
            }
        }
    }
}