import requests

def toChars(word):
    return [char for char in word]

url = "http://localhost:8080"

requests.post(url+ "/debugStart")

go = True
while go:
    status = requests.post(url + "/checkUpdate").json()
    if status['value'] == "Waiting for hand submission":
        print("Please submit your hand: ")

        cards = requests.get(url + "/player/0/getHand").json()
        for i in range(len(cards)):
            print(str(i) + "    " + str(cards[i]))
        numberChoices = input("Which cards do you want?\n")

        indexes = toChars(numberChoices)
        cardChoices = []
        for i in range(5):
            cardChoices.append(cards[int(numberChoices[i])])
        requests.post(url + "/player/0/submitHand", json=cardChoices)
        print("Submitted hand:")
        for i in range(len(cardChoices)):
            print(str(cardChoices[i]))

    else:
        print(status['value'])

    go = input("Press n to stop") != "n"





