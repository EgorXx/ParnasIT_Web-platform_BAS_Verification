rootProject.name = "ParnasIT_Web-platform_BAS_Verification"

include(":backend-api")
include(":backend-core")
include(":backend-security")
include(":backend-persistence")

project(":backend-api").projectDir = file("backend/backend-api")
project(":backend-core").projectDir = file("backend/backend-core")
project(":backend-security").projectDir = file("backend/backend-security")
project(":backend-persistence").projectDir = file("backend/backend-persistence")
