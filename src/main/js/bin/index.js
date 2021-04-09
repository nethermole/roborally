const axios = require('axios');
const readline = require('readline')

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/',
    timeout: 1000
});

async function startGame(){
    const axiosInstance = axios.create({
        baseURL: 'http://localhost:8080/',
        timeout: 1000
    });
    let start = await axiosInstance.post("/start")
}

startGame();

console.log("lets play")
var cards = axiosInstance.get('/player/0/getHand')
console.log(cards)
