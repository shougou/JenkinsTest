def printCommandName(commandName){
    return {
        echo 'The command name is: ' + commandName
    }
}

def test(){
    echo 'Hi, this is pipeline shared library'
}

// def sendEmai(to,subject,body,mineType="text/html"){
def sendEmai(to,subject,body){
    mail to: to,
    // from: '13111002493@163.com'
    subject: subject,
    body: body
    // mineType: mineType
        
}