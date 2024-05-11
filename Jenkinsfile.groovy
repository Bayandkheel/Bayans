pipeline {
    agent any

    tools {
        maven 'Maven 3.6.3'  
        git 'Default'        
    }

    stages {
        stage('Build') {
            steps {
                script {
                    bat 'mvn clean package'
                }
            }
        }

        stage('Unit and Integration Tests') {
            steps {
                script {
                    bat 'mvn test'
                }
            }
        }

        stage('Code Analysis') {
            steps {
                script {
                    bat 'mvn sonar:sonar'
                }
            }
        }

        stage('Security Scan') {
            steps {
                script {
                    bat 'mvn dependency-check:check'
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    bat 'docker build -t myapp .' // Ensure Docker is installed and Jenkins has appropriate permissions
                }
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                script {
                    try {
                        bat 'call deploy-to-staging.bat'
                        bat 'call run-selenium-tests.bat'
                    } catch (Exception e) {
                        echo "Error during staging tests: ${e.getMessage()}"
                        throw e // Rethrow to mark the build as failed
                    }
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    try {
                        bat 'call deploy-to-production.bat'
                    } catch (Exception e) {
                        echo "Deployment to production failed: ${e.getMessage()}"
                        throw e // Rethrow to mark the build as failed
                    }
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
