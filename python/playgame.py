import requests

def toChars(word):
    return [char for char in word]

#   ?seedIn=3960483649208205183
requests.post("http://localhost:8080/debugStart?seedIn=3960483649208205183")

go = True
while go:
    cards = requests.get("http://localhost:8080/player/0/getHand").json()
    for i in range(len(cards)):
        print(str(i) + "    " + str(cards[i]))
    numberChoices = input("Which cards do you want?")
    while len(numberChoices) != 5:
        numberChoices = input("Try again. Which cards do you want?")
    indexes = toChars(numberChoices)
    cardChoices = []
    for i in range(5):
        cardChoices.append(cards[int(numberChoices[i])])

    print("Submitted hand:")
    for i in range(len(cardChoices)):
        print(str(cardChoices[i]))

    requests.post("http://localhost:8080/player/0/submitHand", json=cardChoices)

    go = input("Press n to stop") != "n"





