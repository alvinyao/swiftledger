/**
 * Copyright (c) 2007-2013 Alysson Bessani, Eduardo Alchieri, Paulo Sousa, and the authors indicated in the @author tags
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bftsmart.reconfiguration;

import bftsmart.tom.util.Logger;

/**
 * The type Vm services.
 *
 * @author Andre Nogueira
 */
public class VMServices {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {

        ViewManager viewManager = new ViewManager();

        if (args.length == 1) {
            Logger.println("####Tpp Service[Disjoint]####");

            int smartId = Integer.parseInt(args[0]);

            //			viewManager.removeServer(smartId);
        } else if (args.length == 3) {
            Logger.println("####Tpp Service[Join]####");

            int smartId = Integer.parseInt(args[0]);
            String ipAddress = args[1];
            int port = Integer.parseInt(args[2]);

            //			viewManager.addServer(smartId, ipAddress,port);

        } else {
            Logger.println("Usage: java -jar TppServices <smart id> [ip address] [port]");
            System.exit(1);
        }

        viewManager.executeUpdates();

        Thread.sleep(2000);//2s
        viewManager.close();

        System.exit(0);
    }
}