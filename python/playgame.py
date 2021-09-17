import requests

def toChars(word):
    return [char for char in word]

requests.post("http://localhost:8080/debugStart")

go = True
while go:
    cards = requests.get("http://localhost:8080/player/0/getHand").json()
    for i in range(len(cards)):
        print(str(i) + "    " + str(cards[i]))
    numberChoices = input("Which cards do you want?")
    indexes = toChars(numberChoices)
    cardChoices = [];
    for i in range(5):
        cardChoices.append(cards[int(numberChoices[i])])

    requests.post("http://localhost:8080/player/0/submitHand", json=cardChoices)
    print("Submitted hand:")
    for i in range(len(cardChoices)):
        print(str(cardChoices[i]))

    go = input("Press n to stop") != "n"




