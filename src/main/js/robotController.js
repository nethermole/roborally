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
var cards = async function(){
    return await axiosInstance.get('/player/0/getHand')
}
var submitCards = async function(cardsToSubmit){
    return await axiosInstance.post('/player/0/submitHand', cardsToSubmit)
}


start().then(cards().then(

    function(cardsJson){
        var cardOptions = {};
        for(i = 0; i < cardsJson.data.length; i++){
            cardOptions[i] = cardsJson.data[i]
        }
        console.log(cardOptions)

        rl.question('What cards do you want (comma separated)?', (answer) => {
            var indices = answer.split(',')
            var cardChoices = [];
            for(i = 0; i < 5; i++){
                cardChoices.push(cardsJson.data[indices[i]]);
            }
            console.log(cardChoices)
            submitCards(cardChoices)

            rl.close();
        })
    }

))


