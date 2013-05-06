import scala.collection.mutable.Queue

case class State(id: String, doAction: () => Any, doEntryAction: () => Any, doExitAction: () => Any) extends HSMBase{

  var transitions = new Queue[Transition]()
  var parentState : SubMachineState = null


  def getStates(list : Queue[String]):Queue[String]={
     list += this.id
  }


  def addTransitions(transition : Transition){
     transitions += transition
  }

  def addTransitions(transList : List[Transition]){
    transList.foreach{e => addTransitions(e)}
  }




//---------------Action-Functions------------------------

  override def  getAction:(()=>Any)={
    doAction
  }

   def getEntryAction:(()=>Any)={
    doEntryAction
  }

  def getExitAction:(()=>Any)={
    doExitAction
  }
}