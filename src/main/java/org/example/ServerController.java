package org.example;

import org.example.server.*;

import java.util.Scanner;

public class ServerController {
    public static void main(String[] args) throws Exception {

        int num = -1;
        Scanner sc = new Scanner(System.in);
        while (num != 0) {
            System.out.println("========================");
            System.out.println(" 0 : Quit");
            System.out.println(" 1 : ReqRepBasicServer");
            System.out.println(" 2 : PubSubBasicServer");
            System.out.println(" 3 : PubSubAndPullPushServer");
            System.out.println(" 4 : PubSubAndPullPushServerV2");
            System.out.println(" 5 : DealerRouterAsyncServer");
            System.out.println("========================");

            num = sc.nextInt();
            switch (num) {
                case 0:
                    break;
                case 1:
                    ReqRepBasicServer.start();
                    break;
                case 2:
                    PubSubBasicServer.start();
                    break;
                case 3:
                    PubSubAndPullPushServer.start();
                    break;
                case 4:
                    PubSubAndPullPushServerV2.start();
                    break;
                case 5:
                    DealerRouterAsyncServer.start(new String[]{"1"});
                    break;
                default:
                    System.out.println("1~5 중에 선택해주세요.\n");
            }
        }
    }
}
