from automaton_tools import NFA as MyNFA

def returnNumberNFA(Regex):
    myinput = Regex
    dfa = MyNFA.fromRegex(myinput)

    z=str(dfa.states)
    return z

def GetStatesNFA(Regex):
    myInput = Regex
    dfa = MyNFA.fromRegex(myInput)
    fromTostates = str(dfa.transition)
    fromTostates = fromTostates.replace("{","")
    fromTostates = fromTostates.replace("}","")
    fromTostates = fromTostates.replace("(","")
    fromTostates = fromTostates.split(')')
    string =""
    for i in range(0,len(fromTostates)):
        string +=fromTostates[i]+";"

    string +="&end="+str(dfa.end)+"&start="+str(dfa.start)
    return  string

def endStateNFA(Regex):
    myinput = Regex
    dfa = MyNFA.fromRegex(myinput)
    a = str(dfa.end)
    a = a.replace("{", "")
    a = a.replace("}", "")
    a = a.replace(" ","")
    return a

def StartStateNfa(Regex):
    myinput = Regex
    nfa = MyNFA.fromRegex(myinput)
    a = str(nfa.start)
    return a
