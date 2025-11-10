pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        DOCKER_IMAGE_NAME = 'my-spring-app'
        DOCKER_TAG = "${BUILD_NUMBER}"
        CONTAINER_NAME = 'my-spring-app-container'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    sh "./gradlew test"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE_NAME}:${DOCKER_TAG} ."
                }
            }
        }



        stage('Deploy Locally') {
            steps {
                script {
                    echo "Deploying locally..."
                    sh """
                        docker stop ${CONTAINER_NAME} || true
                        docker rm ${CONTAINER_NAME} || true
                        docker run -d --restart=unless-stopped --name ${CONTAINER_NAME} -p 5000:5000 ${DOCKER_IMAGE_NAME}:${DOCKER_TAG}
                    """
                }
            }
        }
    }

    post {
        always {
            sh "docker system prune -f || true"
        }
        success {
            echo "✅ Сборка и локальный деплой прошли успешно!"
        }
        failure {
            echo "❌ Сборка или деплой не удалась!"
        }
    }
}