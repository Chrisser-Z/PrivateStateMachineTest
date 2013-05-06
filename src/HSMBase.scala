import scala.collection.mutable.Queue

class HSMBase {

  var result = new UpdateResult(None, 0)


  def test() {

  }

  def getAction:(()=>Any)={
   test
  }

  def update(eventID :Int) : UpdateResult = {
    result = new UpdateResult(None, 0)
    result.addActions(getAction)
    result
  }

}


case class UpdateResult(var transition: Option[Transition], var level: Int){
  var actionList = new Queue[(()=>Any)]()

  def addActions(action: () => Any){
    actionList += action
  }




}

