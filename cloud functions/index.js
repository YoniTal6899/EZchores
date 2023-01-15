const functions = require("firebase-functions");
const fs=require('fs'); 
const nodemailer = require('nodemailer');
const admin = require('firebase-admin');
const serviceAccount = require("./admin-service-account-key.json");
const { group } = require("console");
const { user } = require("firebase-functions/v1/auth");
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://ezchores-703ec-default-rtdb.firebaseio.com"
});

exports.sendWelcomeMail =  functions.database.ref('/Users/{userId}').onCreate(async (snapshot,context) => {
    const gmailEmail = "Gmail";
    const gmailPassword = "password";
    const mailTransport = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: gmailEmail,
        pass: gmailPassword,
    },
    });
    console.log('Connected to Gmail');
    var htmlmail= fs.readFileSync("welcome.html", "utf-8").toString();
    const recipentMailJson = await snapshot.ref.get('email');
    const recipentNameJson = await snapshot.ref.get('name');
    const recipentMail = recipentMailJson.val().email 
    const recipentName = recipentNameJson.val().name;
    const mailOptions = {
        from: 'EZchores',
        to: recipentMail,
        subject: `Welcome to EZchores ${recipentName}!`,
        html: htmlmail
    };
    
    try {
        console.log(`Sending mail to: Name: ${recipentName}, Mail: ${recipentMail}`);
        return mailTransport.sendMail(mailOptions);    

    } catch(error) {
        console.error('There was an error while sending the email:', error);
    }
});

exports.CreatedGroupNotification = functions.database.ref('Users/{userId}/Groups/{groupId}')
.onCreate(async(snapshot,context) => {
    let regToken;
    let isAdmin;
    const groupName = await snapshot.val();
    console.log(`groupName is: ${groupName}`);

    const getRegTK = snapshot.ref.parent.parent.child('regTK').once('value', async (snap) => {
        regToken = await snap.val();
        console.log(`token is: ${regToken}`);
    });
    
    const getIsAdmin =  snapshot.ref.parent.parent.parent.parent.child('Groups').child(context.params.groupId).child('admins').once('value', async (snap) => {
        isAdmin = snap.child(context.params.userId).exists();
        console.log(`isAdmin: ${isAdmin}`);
    });
    
    await Promise.all([getRegTK,getIsAdmin]);
    if (isAdmin) {
        console.log('User is admin, not sending notification');
        return;
    }
    const data = {
        message: {
          token: regToken,
          notification: {
            title: `You\'ve been added to a group!`,
            body: groupName,
          },
          data: {
            userId: context.params.userId,
            groupId: context.params.groupId,
          },
        },
      };   
      
    try{
        console.log(`sending: ${JSON.stringify(data.message)}`);
        const msgId = await admin.messaging().send(data.message);
        console.log(`Sent notification succesfully | msgId: ${msgId}` );
    } catch(error) {
        console.error('There was an error while sending the notification:', error);
    }
});

exports.CreatedTaskNotification = functions.database.ref('Users/{userId}/MyTasks/{taskId}')
.onCreate(async(snapshot,context) => {
    let regToken;
    let groupName;
    let taskName;
    let taskVal;
    const groupId = await snapshot.child('groupID').val();

    const getRegTK = snapshot.ref.parent.parent.child('regTK').once('value', async (snap) => {
        regToken = await snap.val();
        console.log(`token is: ${regToken}`);
    });
    const getGroupName = snapshot.ref.parent.parent.child('Groups').child(groupId).once('value', async (snap) => {
        groupName = await snap.val();
        console.log(`groupName is: ${groupName}`);
    });
    const getTaskNameAndVal = snapshot.ref.parent.parent.parent.parent.child('Groups').child(groupId).child('Tasks').child(context.params.taskId).once('value', async (snap) => {
        taskName = await snap.child('name').val();
        console.log(`taskName is: ${taskName}`);
        taskVal = await snap.child('point').val();
        console.log(`taskVal is: ${taskVal}`);
    });
    await Promise.all([getRegTK, getGroupName, getTaskNameAndVal]);
    
    const data = {
        message: {
          token: regToken,
          notification: {
            title: `New task in: ${groupName} worth ${taskVal} points!`,
            body: taskName,
          },
          data: {
            userId: context.params.userId,
            taskId: context.params.taskId,
          },
        },
      };   
      
    try{
        console.log(`sending: ${JSON.stringify(data.message)}`);
        const msgId = await admin.messaging().send(data.message);
        console.log(`Sent notification succesfully | msgId: ${msgId}` );
    } catch(error) {
        console.error('There was an error while sending the notification:', error);
    }
});

exports.CreatedGoalNotification = functions.database.ref('Users/{userId}/MyGoals/{goalId}')
.onCreate(async(snapshot,context) => {
    let regToken;
    let groupName;
    let goalName;
    let goalVal;
    const groupId = await snapshot.child('groupID').val();

    const getRegTK = snapshot.ref.parent.parent.child('regTK').once('value', async (snap) => {
        regToken = await snap.val();
        console.log(`token is: ${regToken}`);
    });
    const getGroupName = snapshot.ref.parent.parent.child('Groups').child(groupId).once('value', async (snap) => {
        groupName = await snap.val();
        console.log(`groupName is: ${groupName}`);
    });
    const getGoalNameAndVal = snapshot.ref.parent.parent.parent.parent.child('Groups').child(groupId).child('Goals').child(context.params.goalId).once('value', async (snap) => {
        goalName = await snap.child('name').val();
        console.log(`goalName is: ${goalName}`);
        goalVal = await snap.child('value').val();
        console.log(`goalVal is: ${goalVal}`);
    });
    await Promise.all([getRegTK, getGroupName, getGoalNameAndVal]);
    
    const data = {
        message: {
          token: regToken,
          notification: {
            title: `New goal in: ${groupName} worth ${goalVal} points!`,
            body: goalName,
          },
          data: {
            userId: context.params.userId,
            goalId: context.params.goalId,
          },
        },
      };   
      
    try{
        console.log(`sending: ${JSON.stringify(data.message)}`);
        const msgId = await admin.messaging().send(data.message);
        console.log(`Sent notification succesfully | msgId: ${msgId}` );
    } catch(error) {
        console.error('There was an error while sending the notification:', error);
    }
});

exports.getUserGroups = functions.https.onCall(async (data, context) => {
    const userId = data.userId;
    let groups = [];
    await admin.database().ref(`/Users/${userId}/Groups`).once('value').then(async snapshot => {
        if (snapshot.val()){
            const groupIds = Object.keys(snapshot.val());
            for (let groupId of groupIds) {
                let group = {};
                group.groupId = groupId;
                group.groupName = snapshot.val()[groupId];
                group.isAdmin = false;
                await admin.database().ref(`/Groups/${groupId}/admins`).once('value').then(snap => {
                    if (snap.hasChild(userId)) {
                        group.isAdmin = true;
                    }
                });
                groups.push(group);
            }
        }
    });
    groups = JSON.stringify(groups);
    console.log(`groups is: ${groups}`);
    return groups;
});


exports.getUserInfo = functions.https.onCall(async (data, context) => {
    let userInfo = {};
    const userId = data.userId;
    
    await admin.database().ref(`/Users/${userId}`).once('value').then(async snapshot => {
        userInfo.curr_points = await snapshot.child('curr_points').val();
        userInfo.email = await snapshot.child('email').val();
        userInfo.name = await snapshot.child('name').val();
        userInfo.image = await snapshot.child('image').val();        
    });

    userInfo = JSON.stringify(userInfo);
    console.log(`userInfo is: ${userInfo}`);
    return userInfo;
});



exports.getAllTasksInGroup = functions.https.onCall(async (data, context) => {
    let tasks = [];
    const groupId = data.groupId;
    
    await admin.database().ref(`/Groups/${groupId}/Tasks`).once('value').then(async snapshot => {
        console.log(`Snapshot val: ${JSON.stringify(snapshot)}`);
        if(snapshot.val()){
            const taskIds = Object.keys(snapshot.val());
            console.log(`taskIds: ${taskIds}`);
            for (let taskId of taskIds) {
                let task = {};
                const isComplete = await snapshot.child(taskId).child('isComplete').val();
                console.log(`isComplete: ${isComplete}`);
                if(!isComplete){
                    task.taskId = taskId;
                    task.taskName = snapshot.child(taskId).child('name').val();
                    task.taskPoints = snapshot.child(taskId).child('point').val();
                    task.assignID = snapshot.child(taskId).child('assignID')
                    console.log(`Adding task: ${JSON.stringify(task)}`);
                    tasks.push(task);
            }
        }
    }
    });

    tasks = JSON.stringify(tasks);
    console.log(`tasks is: ${tasks}`);
    return tasks;
});

exports.getUserTasksInGroup = functions.https.onCall(async (data,context) => {
    let userTasks = [];
    const groupId = data.groupId;
    const userId = data.userId;

    await admin.database().ref(`/Groups/${groupId}/Tasks`).once('value').then(async snapshot => {
        if(snapshot.val()){
            const taskIds = Object.keys(snapshot.val());
            for (let taskId of taskIds) {
                let task = {};
                const isComplete = await snapshot.child(taskId).child('isComplete').val();
                const assignID = await snapshot.child(taskId).child('assignID').val();
                if(!isComplete && assignID === userId){
                    task.taskId = taskId;
                    task.taskName = snapshot.child(taskId).child('name').val();
                    task.taskPoints = snapshot.child(taskId).child('point').val();
                    userTasks.push(task);
                    console.log(`Adding task: ${task}`);
                }
            }
        }
    });
    userTasks = JSON.stringify(userTasks);
    console.log(`userTasks: ${userTasks}`);
    return userTasks;
});




exports.getGroupInfo = functions.https.onCall( async (data,context) => {
    let groupInfo = {};
    const groupId = data.groupId;
    await admin.database().ref(`/Groups/${groupId}`).once('value').then(async (snap) => {
        groupInfo.groupName = snap.child('name').val();
        groupInfo.image = snap.child('image').val();
        groupInfo.userList = snap.child('Friends').val();
    });
    groupInfo = JSON.stringify(groupInfo);
    console.log(`groupInfo is: ${groupInfo}`);
    return groupInfo;
});

exports.getGroupName = functions.https.onCall( async (data,context) => {
    const groupId = data.groupId;
    let groupName;
    await admin.database().ref(`/Groups/${groupId}/name`).once('value').then(async (snap) => {
        groupName = snap.val();
    });
    console.log(`groupName is: ${groupName}`);
    return groupName;
});


exports.getAllGoalsInGroup = functions.https.onCall(async (data, context) => {
    let goals = [];
    const groupId = data.groupId;
    
    await admin.database().ref(`/Groups/${groupId}/Goals`).once('value').then(async snapshot => {
        console.log(`Snapshot val: ${JSON.stringify(snapshot)}`);
        if(snapshot.val()){
            const goalIds = Object.keys(snapshot.val());
            console.log(`taskIds: ${goalIds}`);
            for (let goalId of goalIds) {
                let goal = {};
                const isComplete = await snapshot.child(goalId).child('isComplete').val();
                console.log(`isComplete: ${isComplete}`);
                if(!isComplete){
                    goal.goalId = goalId;
                    goal.goalName = snapshot.child(goalId).child('name').val();
                    goal.goalVal = snapshot.child(goalId).child('value').val();
                    goal.currPoints = snapshot.child(goalId).child('currentPoints')
                    goal.assignID = snapshot.child(goalId).child('assignID')
                    console.log(`Adding goal: ${JSON.stringify(goal)}`);
                    goals.push(goal);
            }
        }
    }
    });

    goals = JSON.stringify(goals);
    console.log(`goals is: ${goals}`);
    return goals;
});

exports.getUserGoalsInGroup = functions.https.onCall(async (data,context) => {
    let userGoals = [];
    const groupId = data.groupId;
    const userId = data.userId;

    await admin.database().ref(`/Groups/${groupId}/Goals`).once('value').then(async snapshot => {
        if(snapshot.val()){
            const goalIds = Object.keys(snapshot.val());
            for (let goalId of goalIds) {
                let goal = {};
                const isComplete = await snapshot.child(goalId).child('isComplete').val();
                const assignID = await snapshot.child(goalId).child('assignID').val();
                if(!isComplete && assignID === userId){
                    goal.goalId = goalId;
                    goal.goalName = snapshot.child(goalId).child('name').val();
                    goal.goalVal = snapshot.child(goalId).child('value').val();
                    goal.currPoints = snapshot.child(goalId).child('currentPoints')
                    goal.assignID = assignID
                    console.log(`Adding goal: ${JSON.stringify(goal)}`);
                    userGoals.push(goal);
                }
            }
        }
    });
    userGoals = JSON.stringify(userGoals);
    console.log(`userTasks: ${userGoals}`);
    return userGoals;
});


exports.getAllUserGoals = functions.https.onCall(async (data,context) => {
    let userGoals = [];
    const userId = data.userId;

    await admin.database().ref(`/Groups`).once('value').then(async snapshot => {
        if(snapshot.val()){
            const groupIds = Object.keys(snapshot.val());
            console.log(`groupIds: ${groupIds}`);
            for (let groupId of groupIds) {
                log(`groupID: ${groupId}`)
                await snapshot.ref.child(groupId).child('Friends').once('value').then(async snap => {
                    snapVal = await snap.val();
                    console.log(`snapVal: ${JSON.stringify(snapVal)}`);
                    if(snap.hasChild(userId)){
                        console.log(`${userId} is member of ${groupId}`);
                        await snap.ref.parent.child('Goals').once('value').then(async sn => {
                            const goalIds = Object.keys(sn.val());
                            for(let goalId of goalIds){
                                let goal = {};
                                const isComplete = await snapshot.child(goalId).child('isComplete').val();
                                const assignID = await snapshot.child(goalId).child('assignID').val();
                                console.log(`assignID: ${assignID}, isComplete: ${isComplete}`);
                                if(!isComplete && assignID === userId){
                                    goal.goalId = goalId;
                                    goal.goalName = sn.child(goalId).child('name').val();
                                    goal.goalVal = sn.child(goalId).child('value').val();
                                    goal.currPoints = sn.child(goalId).child('currentPoints');
                                    goal.assignID = assignID;
                                    goal.groupId = groupId;
                                    goal.groupName = snapshot.child(groupId).child('name').val();
                                    console.log(`Adding goal: ${JSON.stringify(goal)}`);
                                    userGoals.push(goal);
                                }
                            }
                        })
                    }     
                });
    userGoals = JSON.stringify(userGoals);
    console.log(`userGoals: ${userGoals}`);
    return userGoals;
}}})});