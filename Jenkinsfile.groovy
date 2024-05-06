pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    try {
                        sh 'mvn clean package' // Compiles and packages the application using Maven
                        echo "Build completed successfully."
                    } catch (Exception e) {
                        echo "Build failed with error: ${e.getMessage()}"
                        throw e // Ensure the pipeline fails on error
                    }
                }
            }
        }

        stage('Unit and Integration Tests') {
            steps {
                script {
                    try {
                        sh 'mvn test' // Runs unit and integration tests
                        echo "Tests executed successfully."
                    } catch (Exception e) {
                        echo "Testing failed with error: ${e.getMessage()}"
                        throw e // Ensure the pipeline fails on error
                    }
                }
            }
        }

        stage('Code Analysis') {
            steps {
                script {
                    try {
                        sh 'sonar-scanner' // Executes SonarQube scanner for static code analysis
                        echo "Code analysis completed successfully."
                    } catch (Exception e) {
                        echo "Code analysis failed with error: ${e.getMessage()}"
                        throw e // Ensure the pipeline fails on error
                    }
                }
            }
        }

        stage('Security Scan') {
            steps {
                script {
                    try {
                        sh 'run_security_scan' // Placeholder for the security scan script
                        echo "Security scan completed successfully."
                    } catch (Exception e) {
                        echo "Security scan failed with error: ${e.getMessage()}"
                        throw e // Ensure the pipeline fails on error
                    }
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                script {
                    try {
                        sh 'deploy_to_staging.sh' // Deploys the application to a staging environment
                        echo "Deployment to staging was successful."
                    } catch (Exception e) {
                        echo "Deployment to staging failed with error: ${e.getMessage()}"
                        throw e // Ensure the pipeline fails on error
                    }
                }
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                script {
                    try {
                        sh 'run_integration_tests_staging.sh' // Executes integration tests in the staging environment
                        echo "Integration tests on staging completed successfully."
                    } catch (Exception e) {
                        echo "Integration testing on staging failed with error: ${e.getMessage()}"
                        throw e // Ensure the pipeline fails on error
                    }
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    try {
                        sh 'deploy_to_production.sh' // Deploys the application to the production environment
                        echo "Deployment to production was successful."
                    } catch (Exception e) {
                        echo "Deployment to production failed with error: ${e.getMessage()}"
                        throw e // Ensure the pipeline fails on error
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
