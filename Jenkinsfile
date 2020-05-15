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
                        echo 'on Parallel - A'
                    }
                }
                stage('Parallel - B'){
                    //agent
                    steps{
                        echo 'on Parallel - B'
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
}
