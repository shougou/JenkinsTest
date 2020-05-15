pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                echo name
                echo env.PATH
                echo params.name
                script{
                    name == 'lisi'
                    echo name
                }
            }
        }
        stage('Parallel - Staging'){
            failFast true //failFast true 当其中一个进程失败时，强制所有的 parallel 阶段都被终止
            // 声明式流水线的阶段可以在他们内部声明多个嵌套阶段, 它们将并行执行。
            // 一个阶段必须只有一个 steps 或 parallel的阶段。 
            // 嵌套阶段本身不能包含进一步的 parallel 阶段, 但是其他的阶段的行为与任何其他 stage 相同。
            // 任何包含 parallel 的阶段不能包含 agent 或 tools 阶段, 因为他们没有相关 steps。
            parallel{
                stage('Parallel - A'){
                    //agent
                    steps{
                        echo '-- on Parallel - A'
                    }
                }
                stage('Parallel - B'){
                    //agent
                    steps{
                        echo '-- on Parallel - B'
                    }
                }
            } 
        }
        stage('Test') {
            steps {
                echo 'Testing'
            }
        }
        stage('Deploy - Staging') {
             steps {
                echo './deploy staging'
                echo './run-smoke-tests'
            }
        }
    }
    // post 部分定义一个或多个steps 
    // currentBuild.result
    post{
        // 无论流水线或阶段的完成状态如何，都允许在 post 部分运行该步骤。
        always {
            echo '-- This will always run'
        }
        // 只有当前流水线或阶段的完成状态与它之前的运行不同时，才允许在 post 部分运行该步骤。
        changed {
            echo '-- This will run only if the state of the Pipeline has changed'
            echo '-- For example, if the Pipeline was previously failing but is now successful'
        }
        // 只有当前流水线或阶段的完成状态为"failure"，通常web UI是红色。
        failure {
            echo '-- This will run only if failed'
        }
        // 只有当前流水线或阶段的完成状态为"success"，通常web UI是蓝色或绿色。
        success {
            echo '-- This will run only if successful'
            mail to: 13111002493@163.com, subject: 'The Pipeline successed :)'
        }
        // 只有当前流水线或阶段的完成状态为"unstable"，通常由于测试失败,代码违规等造成。通常web UI是黄色。
        unstable {            
            echo '-- This will run only if the run was marked as unstable'
        }
        // 只有当前流水线或阶段的完成状态为"aborted"，通常由于流水线被手动的aborted。通常web UI是灰色。
        aborted {
            echo '-- This will run only if aborted'
        }
    }
}
