package hr.tvz.quiz.rest;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.java_websocket.WebSocket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.tvz.quiz.MainActivity;
import hr.tvz.quiz.MultiPlayerActivity;
import hr.tvz.quiz.R;
import hr.tvz.quiz.UserLocalStore;
import hr.tvz.quiz.model.QuestionsSTOMP;
import hr.tvz.quiz.model.Session;
import hr.tvz.quiz.model.User;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompHeader;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

import static android.R.attr.id;

public class STOMPwebScoket implements Serializable {


    //private static final String BASE_URL = "ws://" + "10.0.3.2" + ":" + "8080" + "/ws/websocket";
    //private static final String BASE_URL = "ws://" + "192.168.1.86" + ":" + "8080" + "/ws/websocket";
    private static final String BASE_URL = "ws://" + "192.168.43.67" + ":" + "8080" + "/ws/websocket";

    private StompClient mStompClient;

    //Subscriptions
    Subscription subsGetQuestion;
    Subscription subsGameInvite;

    private static STOMPwebScoket instance = null;

    private Gson mGson = new GsonBuilder().create();

    private static OnGameStart mListener;

    private UserLocalStore userLocalStore;
    private User user;

    private Context c;

    public static STOMPwebScoket getInstance(Context c, User user) {
        if (instance == null) {
            instance = new STOMPwebScoket(c, user);
        } else if(!instance.c.equals(c)){
            instance.c=c;
            if (c instanceof OnGameStart)
                mListener = (OnGameStart) c;
        }
        return instance;
    }

    private STOMPwebScoket(Context c, User user) {
        this.c = c;

        if (c instanceof OnGameStart)
        mListener = (OnGameStart) c;

        Map<String, String> connectHttpHeaders = new HashMap<String,String>();  ;
        connectHttpHeaders.put("user", user.getName());
        mStompClient = Stomp.over(WebSocket.class, BASE_URL, connectHttpHeaders);


        mStompClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            toast("Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e("STOMP ", "Stomp connection error", lifecycleEvent.getException());
                            toast("Stomp connection error");
                            break;
                        case CLOSED:
                            toast("Stomp connection closed");
                    }
                });
    }

    public void connect(){
        mStompClient.connect();
    }

    public void disconnect(){
        mStompClient.disconnect();
    }

    public void listenGameInvitation(){
        subsGameInvite = mStompClient.topic("/user/queue/invite")  //ToDo: You had an idea to check at every invite of fuction if subsGameInvite is empty...
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("STOMP ", "Received " + topicMessage.getPayload());

                    Session session = mGson.fromJson(topicMessage.getPayload(), Session.class);
                    sendNotification(session, 1);  //ToDo: That is just bad

                    System.out.println("moje" + topicMessage.getPayload());
                });
    }

    public void sendGameInvitation(String recipient, int subject){
        mStompClient.send("/queue/gameInvite", recipient + "/" + String.valueOf(subject))
                .compose(applySchedulers())
                .subscribe(aVoid -> {
                    Log.d("STOMP", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("STOMP", "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                });
    }


    public void sendGetQuestions(String owner, String examId){

        mStompClient.send("/queue/question/" + owner, owner + "/" + examId)
                .compose(applySchedulers())
                .subscribe(aVoid -> {
                    Log.d("STOMP", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("STOMP", "Error send STOMP echo", throwable);
                    toast(throwable.getMessage());
                });
    }

    public void listenGetQuestions(String owner){
        List<StompHeader> headerList = new ArrayList<StompHeader>();
        headerList.add(new StompHeader("owner", owner));

        subsGetQuestion = mStompClient.topic("/user/queue/question/", headerList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(topicMessage -> {
                    Log.d("STOMP ", "Received " + topicMessage.getPayload());

                    QuestionsSTOMP questions = mGson.fromJson(topicMessage.getPayload(), QuestionsSTOMP.class);
                    mListener.OnGameStart(questions);

                    System.out.println("moje" + topicMessage.getPayload());
                });
    }

    public void unsubscribeGetQuestions(){
        subsGetQuestion.unsubscribe();
    }

    public interface OnGameStart {
        void OnGameStart(QuestionsSTOMP questions);
    }

    private void toast(String text) {
        Log.i("STOMP ", text);
        Toast.makeText(c, text, Toast.LENGTH_SHORT).show();
    }

    protected <T> Observable.Transformer<T, T> applySchedulers() {
        return rObservable -> rObservable
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public StompClient getmStompClient() {
        return mStompClient;
    }

    public void setC(Context c) {
        this.c = c;
    }


    private void sendNotification(Session session, int examId){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c);
        builder.setSmallIcon(R.mipmap.ic_settings_black_24dp)
                .setLargeIcon(BitmapFactory.decodeResource(c.getResources(), R.mipmap.ic_home_black_24dp))
                //.setColor(getResources().getColor(R.color.primary))
                .setContentTitle("Quiz4Students")
                .setContentIntent(createPendingIntent(session, examId))
                .setContentText(session.getUsername() + " želi igrati sa vama.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX);

        NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, builder.build());

    }

    private PendingIntent createPendingIntent(Session session, int examId){
        Intent resultIntent = new Intent(c, MultiPlayerActivity.class);
        Intent main = new Intent(c, MainActivity.class);

        resultIntent.putExtra("OPPONENT", session.getUsername());
        resultIntent.putExtra("SUBJECT_ID", session.getSubjectId());
        resultIntent.putExtra("EXAM_ID", examId);
        //activity.finish();


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(c);
        // Adds the back stack
        stackBuilder.addParentStack(MultiPlayerActivity.class);

        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        //Bundle remoteInput = RemoteInput.getResultsFromIntent(resultIntent);
        return resultPendingIntent;
    }
}
