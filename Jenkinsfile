#!groovy
@Library('jenkinslib@master') _ //加载
// @Library("jenkinslib@$BRANCH_NAME") 版本可以被 computed， 而不是像注释那样的常量
// _ 等同于import org.devops.tools  不建议 import 一个全局变量/函数, 由于这将强制编译器 将字段和方法解释为 static。 即使他们是要实例化的。
// @Library(['my-shared-library', 'otherlib@abc1234']) _  //一条语句访问多个库

def tools = new org.devops.tools() //src
hello() //要定义 hello, 需要创建 vars/hello.groovy`文件并实现`call 方法。
hello('Joe')

String jenkinsUrl = '/home/app/jenkins/'

pipeline {
    agent any //必须在pipeline块内的顶层定义，stage块内的agent是可选的

    // 定义流水线运行时的配置选项 test123
    options {
        //持久化工件和控制台输出，用于保存Pipeline最近几次运行的数据
        buildDiscarder(logRotator(numToKeepStr: '1')) //默认几次？
        disableConcurrentBuilds() //不允许并行执行Pipeline。可用于防止同时访问共享资源等。
        retry(3) //如果失败，请按指定的次数重试整个管道。
        skipStagesAfterUnstable() //一旦构建状态进入了“不稳定”状态，就跳过stage。
        timeout(time:1,unit:'HOURS') //设置流水线运行的超时时间, 在此之后，Jenkins将中止流水线。 状态为aborted
    }

    //environment顶级pipeline块中使用的指令将适用于Pipeline中的所有步骤
    //在一个stage中定义的environment指令只将给定的环境变量应用于该stage中的步骤
    //environment具有一个帮助方法credentials()，可用于在Jenkins环境中通过其标识符访问预定义的凭据
    environment { 
        CC = 'clang'
    }

    // 首次执行无需选择参数，首次执行会生成Jenkins的参数选择块的内容，缺省值作为参数。首次执行之后，Job参数的设定也已经生成，再次执行的时候，输入参数的选择则会生效
    parameters {
        // string 字符串类型参数
        // text	文本类型参数，与字符串的区别在于可以包含多行信息，用于传入较多信息输入
        // booleanParam	布尔类型参数
        // choice	类似下拉框或者支持多值的单选参数
        // file	指定构建过程中所需要的文件
        // password 考虑到安全的因素，需要通过参数方式传递的密码类型

        choice(
            description: '你需要选择哪个模块进行构建 ?',
            name: 'modulename',
            choices: ['Module1', 'Module2', 'Module3']
        )

        string(
                description: '当前所属stage ?',
                name: 'variable', 
                defaultValue: 'zhangsan'
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
    // stages 在pipeline内只有一次
    stages {
        stage('script - stage') {
            steps {
                script {
                    def browsers = ['chrome', 'firefox']
                    for (int i = 0; i < browsers.size(); ++i) {
                        tools.PrintMes("Testing the ${browsers[i]} browser",'red')
                    }
                }
            }
        }
        stage('sharelib - stage') {
            // stage的options指令类似于Pipeline根目录中的options指令。但是，stage的 options只能包含与stage相关的步骤，如retry，timeout或timestamps，或声明性选项，如skipDefaultCheckout。
            // 在stage内，options在进入agent或检查任何when条件之前调用指令中的步骤。
            options {
                timeout(time: 1, unit: 'HOURS') 
            }

            when { 
                environment name: 'variable', value: 'build'
            }

            // steps部分必须包含一个或多个步骤
            steps {
                //声明式流水线不允许在`script`指令之外使用全局变量
                script{ 
                    tools.PrintMes("获取代码",'green')
                    // tools.PrintMes(hello(),'green')
                    // tools.PrintMes(hello('Joe'),'green')
                    hello()
                    hello('Joe')
                }
            }
        }
        stage('printParameter - Staging') {
            steps {
                // // echo env.PATH 打印环境变量
                echo "参数：${params}"
                echo "用户名1 ${deploy_username}"
                echo "用户名2 ${params.deploy_username}"
                echo "用户名3 " + params.deploy_username
                echo "用户名4 " + deploy_username
                // echo '参数：${params}' //错误用法
                // echo '参数：' params //错误用法
                
                echo "printParameter stage: 部署机器的名称 : ${params.deploy_hostname} ..."
                echo "DeprintParameterploy stage: 部署机器的用户名 : ${params.deploy_username} ..." 
                echo "printParameter stage: 部署连接的密码 : ${params.deploy_password} ..." 
                echo "printParameter stage: Release Note的信息为 : ${params.release_note} ..." 
                echo "printParameter stage: 选中的构建Module为 : ${params.modulename} ..."

                echo "变量值：${variable} (默认值zhangsan)"
                script{ 
                    tools.PrintMes("变量值：${variable} (zhangsan)",'green')
                }
            }
        }
        stage('setVarVal - Staging') {
            steps {
                echo "用户名5 ${params.deploy_username}"
                echo "Test stage: 是否执行自动化测试: ${params.test_skip_flag} ..."
                echo 'Testing'

                script{
                    
                    sh "chmod +x ./shfolder/first.sh"
                    sh "chmod +x ./shfolder/second.sh"
                    sh "chmod +x ./shfolder/three.sh"

                    tools.PrintMes("变量值：${variable} (zhangsan)",'green')

                    variable=sh(script: "./shfolder/first.sh ${variable}", returnStdout: true).trim()
                    tools.PrintMes("变量值：${variable} (lisi2)",'green')
                }
            }
        }
         stage('verifyVariable - Staging') {
            steps {
                script{ 
                    tools.PrintMes("变量值：${variable} (wangwu)",'green')
                }
            }
        }
        stage('Parallel - Staging'){
            failFast true //failFast true 当其中一个进程失败时，强制所有的 parallel 阶段都被终止
            // 一个stage有且之只能有一个 steps /stages 或 parallel的阶段。 
            // 嵌套的stages 本身不能包含进一步的 parallel, 但是其他的阶段的行为与任何其他 stage 相同，包括顺序执行的stage列表stages。
            // 任何包含 parallel 的阶段不能包含 agent 或 tools 阶段, 因为那些和steps没有关系。 ?
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
    // currentBuild.result???
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
