
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class GCMSend {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GCMSend send = new GCMSend();
		send.sendMessage("Hello   world");
	}

	public void sendMessage(String msg){
		try{
			String serverKey = "AIzaSyCGmT4rgD_Yj1LyN8QPNVg2krZc4P-dN8o";//serverkey 就是你在API Project中API Access创建server key之后得到的API Key
			String regId = "APA91bG29LnV97N8zD6DNoOzKu06RAq1C7uCRN_YTd53aWYGpoprGr6WwkaPNIHjSSGELySLB5tf1lx5jgxrGTSCnc8B-AJxZu8ivUxrXsgohTAqOAD7-Z3-JmIsldPCWkeVOW0tsD797IPSNbbxyq_UesgK4KsIMA";	// 你的设备中的app向GCM注册得到的值,  即 GCMRegistrar.getRegistrationId(this);
			Sender sender = new Sender(serverKey);
			Message message = new Message.Builder()
		    						.addData("msg", msg)
		    						.build();
			Result result = sender.send(message, regId, 5);
			String status = "Sent message to one device: " + result;
			System.out.println(status);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
