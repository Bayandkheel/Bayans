pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'mvn clean package' // Compiles and packages the application using Maven
                }
            }
        }

        stage('Unit and Integration Tests') {
            steps {
                script {
                    sh 'mvn test' // Runs unit and integration tests
                }
            }
        }

        stage('Code Analysis') {
            steps {
                script {
                    sh 'sonar-scanner' // Executes SonarQube scanner for static code analysis
                }
            }
        }

        stage('Security Scan') {
            steps {
                script {
                    sh 'run_security_scan' // Placeholder for the security scan script
                }
            }
        }

        stage('Deploy to Staging') {
            steps {
                script {
                    sh 'deploy_to_staging.sh' // Deploys the application to a staging environment
                }
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                script {
                    sh 'run_integration_tests_staging.sh' // Executes integration tests in the staging environment
                }
            }
        }

        stage('Deploy to Production') {
            steps {
                script {
                    sh 'deploy_to_production.sh' // Deploys the application to the production environment
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
