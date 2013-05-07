

class A {

  val aa = 5
}

trait B_base extends A{
  val bb = "hello"

  def toImplement(s :String): Boolean

  def implemented() {println("imple")}
}


class B extends B_base

class C() extends A with B_base   {
  def toImplement(s: String): Boolean = false
}


case class Tate() {

  var id = ""

  def withid(id: String) = {
    val res = Tate()
    res.id = id
    res
  }

  andEnteringFunction(f: () => Unit) {

  }

}

object Main {

  Tate withid "bla" andEnteringFunction doA

  val x = new C()

  x.implemented()

  val base = new HSMBase
  val stateB = new State(id = "B", doB, enterB, exitB)
  val stateC = new State("C", doC, enterC, exitC)
  val stateD = new State("D", doD, enterD, exitD)
  val stateE = new State("E", doE, enterE, exitE)
  val stateA : SubMachineState = new SubMachineState("A", Some(stateC), doA, enterA, exitA)


  val transBtoC = new Transition(1, 0, stateC, doTrans1)
  val transCtoD = new Transition (2, 1, stateD, doTrans2)
  val transDtoC = new Transition (3, -1, stateC, doTrans3)
  val transAtoE = new Transition(4, 0, stateE, doTrans4)


  val stateMachine = new HierarchicalStateMachine(stateA)





  def main(args: Array[String]) {
    stateB.parentState = stateA
    stateC.parentState = stateA
    stateA.parentMachine = stateMachine
    stateB.addTransitions(transBtoC)
    stateC.addTransitions(transCtoD)
    stateD.addTransitions(transDtoC)
    stateA.addTransitions(transAtoE)
    println(stateMachine.getCurrentStateQueue)
    println("--------------------------------------")
    stateMachine.update(4)
    executeResults()


  }

  def executeResults(){
    for (e <- stateMachine.result.actionList){
      e()
    }
  }






  def doA() {
    println("DoingA")
  }

  def enterA() {
    println("EnterA")
  }

  def exitA() {
    println("ExitA")
  }
  def doD() {
    println("DoingD")
  }

  def enterD() {
    println("EnterD")
  }

  def exitD() {
    println("ExitD")
  }

  def doB() {
    println("DoingB")
  }

  def enterB() {
    println("EnterB")
  }

  def exitB() {
    println("ExitB")
  }

  def doC() {
    println("DoingC")
  }

  def enterC() {
    println("EnterC")
  }

  def exitC() {
    println("ExitC")
  }

  def doE() {
    println("DoingE")
  }

  def enterE() {
    println("EnterE")
  }

  def exitE() {
    println("ExitE")
  }

  def doTrans1() {
    println("DoingTrans1")
  }

  def doTrans2() {
    println("DoingTrans2")
  }

  def doTrans3() {
    println("DoingTrans3")
  }

  def doTrans4() {
    println("DoingTrans4")
  }

}

