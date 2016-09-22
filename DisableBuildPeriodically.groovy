import hudson.model.*
import hudson.triggers.*


TriggerDescriptor SCM_TRIGGER_DESCRIPTOR = Hudson.instance.getDescriptorOrDie(TimerTrigger.class)
assert SCM_TRIGGER_DESCRIPTOR != null;

for(item in Hudson.instance.items)
{
  println("Working on project <$item.name>")
  
  def trigger = item.getTriggers().get(SCM_TRIGGER_DESCRIPTOR)
  
  if(trigger != null && trigger instanceof TimerTrigger)
  {
    print("> $trigger.spec")
    
    item.removeTrigger(SCM_TRIGGER_DESCRIPTOR)
    
  }
  else
  {
    println "> Nothing to do"
  }
}
