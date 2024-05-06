pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package' // Assuming Maven is used
            }
        }

        stage('Unit and Integration Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Code Analysis') {
            steps {
                sh 'sonar-scanner'
            }
        }

        stage('Security Scan') {
            steps {
                sh 'run_security_scan'
            }
        }

        stage('Deploy to Staging') {
            steps {
                sh 'deploy_to_staging.sh'
            }
        }

        stage('Integration Tests on Staging') {
            steps {
                sh 'run_integration_tests_staging.sh'
            }
        }

        stage('Deploy to Production') {
            steps {
                sh 'deploy_to_production.sh'
            }
        }
    }

    post {
        always {
            mail bcc: '', body: "Pipeline completed. See details: ${env.BUILD_URL}", from: '', replyTo: '', subject: "Pipeline Notification: ${currentBuild.fullDisplayName}", to: "email@example.com"
        }
    }
}
