rootProject.name = "CraftHammer"

include(":LangPack")
include(":LangPack:Core")
include(":LangPack:CraftHammer")

include(":CraftHammer_Util") // Universal utilities for all modules.
include(":CraftHammer_API")  // Universal API for CraftHammer & SledgeHammer.
include(":CraftNail")        // Private repository for CraftHammer. (Legal reasons)
include(":CraftHammer")      // Public repository for CraftHammer.
include(":SledgeHammer")     // Plugin API for CraftHammer.
include(":SledgeHammer:ExamplePlugin")
