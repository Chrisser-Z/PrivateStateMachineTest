
case class Transition(id: Int, lvl : Int, targetState : State, doAction: () => Any) {


  def getLevel:Int={
    lvl
  }

  def isTriggered(eventID : Int):Boolean={
    if (eventID == id){
      true
    } else {
      false
    }
  }

  def getAction:(()=>Any)={
    doAction
  }

}
