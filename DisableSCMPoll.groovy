import hudson.model.*
import hudson.triggers.*


TriggerDescriptor SCM_TRIGGER_DESCRIPTOR = Hudson.instance.getDescriptorOrDie(SCMTrigger.class)
assert SCM_TRIGGER_DESCRIPTOR != null;

for(item in Hudson.instance.items)
{
  println("Working on project <$item.name>")
  
  def trigger = item.getTriggers().get(SCM_TRIGGER_DESCRIPTOR)
  if(trigger != null && trigger instanceof SCMTrigger)
  {
    print("> $trigger.spec")
    
    item.removeTrigger(SCM_TRIGGER_DESCRIPTOR)
    
  }
  else
  {
    println "> Nothing to do"
  }
}
