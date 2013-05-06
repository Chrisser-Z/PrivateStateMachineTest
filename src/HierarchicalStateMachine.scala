import scala.collection.mutable.Queue
import scala.util.control.Breaks._


case class HierarchicalStateMachine(initialState : State) extends HSMBase {

  var states : List[State] = null



  var currentState : Option[State] = Some(initialState)
  var triggeredTransition : Option[Transition] = None



  def getCurrentStateQueue:Queue[String]={
    val list = new Queue[String]()

    currentState match {
      case Some(s) => s.getStates(list)
      case None => list
    }

    /*if (currentState != null){
      currentState.getStates(list)
    } else {
      list
    } */

  }



  override def  update(eventID : Int):UpdateResult={


    // if no currentState, use initialState
    currentState match {
      case None =>
        currentState = Some(initialState)
        result.addActions(currentState.get.getEntryAction)
      case Some(s) =>
    }

     /*if (currentState == null){
       currentState = initialState
        result.addActions(currentState.getEntryAction)
     } */

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

    /*if (triggeredTransition != null){
      result = new UpdateResult(triggeredTransition, triggeredTransition.getLevel)
    } else {
      result = currentState.get.update(eventID)
    }
    */

    //check if result contains transition
   // if (result.transition != null){
    println(result.transition)
    result.transition match {
      case Some(t) =>
        if (result.level == 0){

          val targetState = t.targetState
          result.addActions(currentState.get.getExitAction)
          result.addActions(t.getAction)
          result.addActions(targetState.getEntryAction)

          currentState = Some(targetState)

          result.addActions(getAction)

          result.transition = null
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


    currentState match {
      case Some(s) => list += s.getExitAction
      case None =>
    }
    /*
    if (currentState != null){
      list += currentState.get.getExitAction
    } */

    currentState = Some(state)
    list += state.getEntryAction
  }




}
