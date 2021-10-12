from automaton_tools import DFA as MyDFA
import rstr
from xeger import Xeger

def returnNumber(Regex):
    myinput = Regex
    dfa = MyDFA.fromRegex(myinput)

    z=str(dfa.states)
    return z



def GetStates(Regex):

    dfa = MyDFA.fromRegex(Regex)
    fromTostates = str(dfa.transition)
    fromTostates = fromTostates.replace("{","")
    fromTostates = fromTostates.replace("}","")
    fromTostates = fromTostates.replace("(","")
    fromTostates = fromTostates.split(')')
    string =""
    for i in range(0,len(fromTostates)):
        string +=fromTostates[i]+";"
    return  string



def endState(myinput):
    dfa = MyDFA.fromRegex(myinput)
    a = str(dfa.end)
    a = a.replace("{", "")
    a = a.replace("}", "")
    a = a.replace(" ","")
    return a



def RandomRegex(Regex):
    x = Xeger(limit=5)  # default limit = 10
    i=0
    a = ""
    while i<5:
        a+=x.xeger(Regex)+"\n"
        i = i+1

    return  a