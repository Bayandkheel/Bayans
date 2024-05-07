pipeline {
    agent any

    tools {
        maven 'Maven 3.6.3'
    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Unit and Integration Tests') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
        }

        stage('Code Analysis') {
            steps {
                script {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Security Scan') {
            steps {
                script {
                    sh 'mvn dependency-check:check'
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    sh 'docker build -t myapp .'
                }
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                script {
                    sh './deploy-to-staging.sh'
                    sh './run-selenium-tests.sh'
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    sh './deploy-to-production.sh'
                }
            }
        }
    }

    post {
        success {
            mail to: 'bayandkheel12@gmail.com',
                 subject: "Success: ${currentBuild.fullDisplayName}",
                 body: "The build was successful. Check the output at ${env.BUILD_URL}."
        }
        failure {
            mail to: 'bayandkheel12@gmail.com',
                 subject: "Failed: ${currentBuild.fullDisplayName}",
                 body: "The build failed. Check the output at ${env.BUILD_URL}."
        }
    }
}
