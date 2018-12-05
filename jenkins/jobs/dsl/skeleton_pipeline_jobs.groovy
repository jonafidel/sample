// Folders
//def workspaceFolderName = "${WORKSPACE_NAME}"
def projectFolderName = "${PROJECT_NAME}"

// Jobs
def cartridge_build = freeStyleJob(projectFolderName + "/build_job")

// Views
def pipelineView = buildPipelineView(projectFolderName + "/Sample-Pipeline")

pipelineView.with{
    title('Sample-Pipeline')
    displayedBuilds(10)
    selectedJob(projectFolderName + "/build_job")
    showPipelineParameters()
    showPipelineDefinitionHeader()
    refreshFrequency(5)
}


cartridge_build.with{


  scm {
    git {           
      remote {
        credentials('ldap-admin')
        url('http://13.52.66.98/gitlab/adminsexy/CurrencyConverterDTS.git')
        }
      branch('*/master')
    }
  }

  wrappers {
    preBuildCleanup()
  }

  triggers {
    gitlabPush {
      buildOnMergeRequestEvents(true)
      buildOnPushEvents(true)
      enableCiSkip(true)
      setBuildDescription(false)
      rebuildOpenMergeRequest('never')
    }

    steps {
    shell('''
mkdir target
tar --exclude='./.git' --exclude='./target' -zcvf ./target/sample.tar .''')
    }

   
  }
}