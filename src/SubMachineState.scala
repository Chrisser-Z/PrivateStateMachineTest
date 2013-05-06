import scala.collection.mutable.Queue
import scala.util.control.Breaks._

class SubMachineState(id : String, initialState : Option[State], doAction: () => Any, doEntryAction: () => Any, doExitAction: () => Any) extends State(id : String, doAction: () => Any, doEntryAction: () => Any, doExitAction: () => Any) {

  var states : List[State] = null
  var currentState : Option[State] = initialState
  var triggeredTransition : Option[Transition] = None
  var parentMachine : HierarchicalStateMachine = null



  override def getStates(list : Queue[String]):Queue[String]={
    currentState match {
      case Some(s) => s.getStates(list += this.id)
      case None => list
    }

    /*if (currentState != null){
      currentState.getStates(list += this.id)
    } else {
      return list
    }  */

  }

  override def update(eventID : Int):UpdateResult={

    // if no currentState, use initialState
    currentState match {
      case None =>
        currentState = initialState
        result.addActions(currentState.get.getEntryAction)
      case Some(s) =>
    }

    /*if (currentState == null){
      currentState = initialState
      result.addActions(currentState.getEntryAction)
    }    */

    // try finding  a transition in currentState

    triggeredTransition = None
    breakable{
      for (transition <- currentState.get.transitions){
        if (transition.isTriggered(eventID)){
          triggeredTransition = Some(transition)
          break()
        }
      }
    }

    // if triggeredTransition create result
    triggeredTransition match {
      case Some(t) => result = new UpdateResult(triggeredTransition, t.getLevel)
      case None => result = currentState.get.update(eventID)
    }

    /*
    if (triggeredTransition != null){
      result = new UpdateResult(triggeredTransition, triggeredTransition.getLevel)
    } else {
      result = currentState.get.update(eventID)
    } */


    //check if result contains transition

    result.transition match {
      case Some(t) =>
        if (result.level == 0){

          val targetState = t.targetState
          result.addActions(currentState.get.getExitAction)
          result.addActions(t.getAction)
          result.addActions(targetState.getEntryAction)

          currentState = Some(targetState)

          result.addActions(getAction)

          result.transition = None
        } else if (result.level >= 1){

          result.addActions(currentState.get.getExitAction)
          currentState = None
          result.level = result.level - 1


        } else {
          val targetState = t.targetState
          val targetMachine : SubMachineState = targetState.parentState
          result.addActions(t.getAction)
          targetMachine.updateDown(targetState, -result.level, result.actionList)
        }


      //} else {
      case None => result.addActions(getAction)
    }

    result

  }


  def updateDown(state: State, lvl: Int, list : Queue[(()=>Any)] ){

    if (lvl > 0 && lvl != 1){
      parentState.updateDown(this, lvl-1, list)
    } else if (lvl == 1){
       parentMachine.updateDown(this, lvl-1, list)
    }


    currentState match {
      case Some(s) => list += s.getExitAction
      case None =>
    }

    /*
    if (currentState != null){
      list += currentState.get.getExitAction
    }   */

    currentState = Some(state)
    list += state.getEntryAction
  }



}
