#!groovy
@Library('jenkinslib@master') _ //加载
// @Library("jenkinslib@$BRANCH_NAME") 版本可以被 computed， 而不是像注释那样的常量
// _ 等同于import org.devops.tools  不建议 import 一个全局变量/函数, 由于这将强制编译器 将字段和方法解释为 static。 即使他们是要实例化的。
// @Library(['my-shared-library', 'otherlib@abc1234']) _  //一条语句访问多个库

def tools = new org.devops.tools() //src
hello() //要定义 hello, 需要创建 vars/hello.groovy`文件并实现`call 方法。
hello('Joe')

String workspace = '/home/app/jenkins/'

pipeline {
    agent any
    // 定义流水线运行时的配置选项 
    options {
        // timeout: 设置流水线运行的超时时间, 在此之后，Jenkins将中止流水线。 状态为aborted
        timeout(time:1,unit:'HOURS')
    }
    // 首次执行无需选择参数，首次执行会生成Jenkins的参数选择块的内容，缺省值作为参数。首次执行之后，Job参数的设定也已经生成，再次执行的时候，输入参数的选择则会生效
    // 构建参数的时候怎么修改默认参数值？
    parameters {
        // string 字符串类型参数
        // text	文本类型参数，与字符串的区别在于可以包含多行信息，用于传入较多信息输入
        // booleanParam	布尔类型参数
        // choice	类似下拉框或者支持多值的单选参数
        // file	指定构建过程中所需要的文件
        // password 考虑到安全的因素，需要通过参数方式传递的密码类型

        // string (name:'user',defaultValue:'hanpl',description:'')
        // string (name:'manager',defaultValue:'zongzy',description:'')
        // booleanParam (name:'isFriend',defaultValue:true,description:'')

        choice(
            description: '你需要选择哪个模块进行构建 ?',
            name: 'modulename',
            choices: ['Module1', 'Module2', 'Module3']
        )

        string(
                description: '当前所属stage ?',
                name: 'stageName', 
                defaultValue: 'build'
        )

        string(
                description: '你需要在哪台机器上进行部署 ?',
                name: 'deploy_hostname', 
                defaultValue: 'host131'
        )
       
        string(
                description: '部署机器连接时需要用到的用户名是什么 ?',
                name: 'deploy_username', 
                defaultValue: 'admin'
        )

        text(
            name: 'release_note', 
            defaultValue: 'Release Note 信息如下所示: \n \
            Bug-Fixed: \n \
            Feature-Added: ', 
            description: 'Release Note的详细信息是什么 ?'
        )

        booleanParam(
            name: 'test_skip_flag', 
            defaultValue: true, 
            description: '你需要在部署之前执行自动化测试么 ?'
        )

        password(
            name: 'deploy_password', 
            defaultValue: 'liumiaocn', 
            description: '部署机器连接时需要用到的密码信息是什么 '
        )

        // 文件怎么用
        file(
            name: "deploy_property_file", 
            description: "你需要输入的部署环境的设定文件是什么 ?"
        )
    }
    stages {
        stage('ShareLibrary') {
            when { 
                environment name: 'stageName', value: 'build'
            }
            steps {
                //声明式流水线不允许在`script`指令之外使用全局变量
                script{ 
                    tools.PrintMes("获取代码",'green')
                    hello()
                    hello('Joe')
                }
            }
        }
        stage('Build - Staging') {
            steps {
                // // echo env.PATH 打印环境变量

                // script{
                //     // name赋值，不正确不生效
                //     name == 'lisi'
                //     echo name
                // }
                
                echo "参数：${params}"
                echo "用户名1 ${deploy_username}"
                echo "用户名2 ${params.deploy_username}"
                echo "用户名3 " + params.deploy_username
                echo "用户名4 " + deploy_username
                // echo '参数：${params}' //错误用法
                // echo '参数：' params //错误用法

                echo "Build stage: 选中的构建Module为 : ${params.modulename} ..."
                echo 'Building' 
                echo "当前所属阶段：${name} (默认值)"
            }
        }
        stage('Test - Staging') {
            steps {
                echo "用户名5 ${params.deploy_username}"
                echo "Test stage: 是否执行自动化测试: ${params.test_skip_flag} ..."
                echo 'Testing'

                script{
                    echo "当前所属阶段：${name} (默认值)"
                    // sh '/home/app/jenkins/testreturn.sh > commandResult'
                    sh "${workspace}testreturn.sh > commandResult"
                    name=readFile('commandResult').trim()
                    echo "${name}" // 返回值应该是lisi
                    name=sh(script: "/home/app/jenkins/testreturn2.sh", returnStdout: true).trim()
                    echo "${name}" // 返回值应该是wangwu
                }
            }
        }
        stage('Deploy - Staging') {
             steps {
                echo "${name}"
                echo "Deploy stage: 部署机器的名称 : ${params.deploy_hostname} ..."
                echo "Deploy stage: 部署机器的用户名 : ${params.deploy_username} ..." 
                echo "Deploy stage: 部署连接的密码 : ${params.deploy_password} ..." 
                echo "Deploy stage: Release Note的信息为 : ${params.release_note} ..." 
                echo './deploy staging'
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
        stage('RetryAndTimeout - Staging') {
            steps {
                retry(3) {
                    // 没有这个文件的话会尝试3次，然后流水线状态为失败
                    // sh './flakey-deploy.sh'
                }
                // timeout: 设置流水线运行的超时时间, 在此之后，Jenkins将中止流水线。
                timeout(time: 3, unit: 'MINUTES') {
                    // sh './health-check.sh'
                }
            }
            post {
                    always {
                        echo 'post in stage'
                    }
                }
        }
    }
    // post可以放在顶层，也就是和agent{…}同级，也可以放在stage里面
    // post部分定义Pipeline运行或阶段结束时运行的操作。
    // currentBuild.result
    post{
        // 无论流水线或阶段的完成状态如何，都允许在 post 部分运行该步骤。
        always {
            //archiveArtifacts artifacts: 'build/libs/**/*.jar', fingerprint: true
            //junit 'build/reports/**/*.xml'
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
            // 配置邮箱  没有配置邮箱地址
            mail to: '13111002493@163.com',
            subject: "successed Pipeline: ${currentBuild.fullDisplayName}",
            body: "Something is right with ${env.BUILD_URL}"
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
