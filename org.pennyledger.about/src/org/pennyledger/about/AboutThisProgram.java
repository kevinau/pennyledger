package org.pennyledger.about;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(configurationPolicy=ConfigurationPolicy.IGNORE, immediate=true)
public class AboutThisProgram {

  private static Logger logger = LoggerFactory.getLogger(AboutThisProgram.class);
  
  private static final String version = "1.0.0";
  
  private static String[] text = {
    "",
    "PennyLedger, version " + version,
    "Copyright (c) Kevin Holloway 2013-2016",
    "",
    "This Source Code Form is subject to the terms",
    "of the Mozilla Public License, version 2.0.",
    "If a copy of the MPL was not distributed with this",
    "file, You can obtain one at http://mozilla.org/MPL/2.0/",
  };


  @Activate
  protected void s () {
    for (String x : text) {
      logger.info(x);
    }
  }

  @Deactivate
  protected void deactivate () {
  }
}
