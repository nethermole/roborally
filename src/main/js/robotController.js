const axios = require('axios');
const readline = require('readline')
const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/',
    timeout: 0
})

var start = async function (){
    var started = await axiosInstance.post('/start')
}
var cards = async function(player){
    console.log('getting cards for player: ' + player)
    return await axiosInstance.get('/player/' + player + '/getHand')
}
var submitCards = async function(player, cardsToSubmit){
    return await axiosInstance.post('/player/' + player + '/submitHand', cardsToSubmit)
}

var userInput = function waitForUserInput(output){
    return new Promise(resolve => rl.question(output, answer => {
        resolve(answer);
    }))
}

function sleep(ms){
    return new Promise(resolve => setTimeout(resolve, ms));
}

var doCardCycle = async function(player, cardsJson){
    var cardOptions = {};
    for(i = 0; i < cardsJson.data.length; i++){
        cardOptions[i] = cardsJson.data[i]
    }
    console.log(cardOptions)

    //rl.question('What cards do you want (comma separated)?', (answer) => {
        //var indices = answer.split(',')
        var indices = [0,1,2,3,4];
        var cardChoices = [];
        for(i = 0; i < 5; i++){
            cardChoices.push(cardsJson.data[indices[i]]);
        }
        console.log(cardChoices)
        submitCards(player, cardChoices)
    //})
}

async function main(player){
    for(let i = 0; i < 100; i++){
        const cardsJson = await cards(player);
        doCardCycle(player, cardsJson);
        await sleep(1000)
    }
}

start();
for(let i = 0; i < 8; i++){
    main(i);
}







