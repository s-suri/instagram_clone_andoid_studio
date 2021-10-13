package com.some.studychats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Log;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.some.studychats.Model.Chat;
import com.some.studychats.ModelInstagram.Comment;
import com.some.studychats.ModelInstagram.User;
import com.some.studychats.Notifications.APIService;
import com.some.studychats.Notifications.Client;
import com.some.studychats.Notifications.Data;
import com.some.studychats.Notifications.GroupApiService;
import com.some.studychats.Notifications.MyResponse;
import com.some.studychats.Notifications.Sender;
import com.some.studychats.Notifications.Token;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MassageActivityInstagram extends AppCompatActivity {

    String names[] = {"Hi", "Hello", "Aap kese ho? (How are you)", "Me thik hu. (I am fine)", "tum kese ho. (How are you)", "shubh prabhaat (good morning)",
            "Ganbhir bano. (Be serious.)", "Haan zarur! (Certainly!)", "Ise saaf karo. (Clean it.)", "Ab chahe jo ho! (Come what may!)",
            "Koi bat nahi. (It’s all right.)", "Shararti mat bano. (Don’t be naughty.)", "Yah bahut dur hai. (It is so far)", "Turant jao. (Go right away)",
            "Dur jao. (Go away.)", "Seedhe jaana. (Go Straight.)", "Koi bat nahi. (No problem.)", "Nahi kabhi nahi. (Not at all.)", "Aur kuch nahi. (Nothing else.)",
            "Koi khas bat nahi. (Nothing special.)", "Bharosa rakhe. (Rest Assured.)", "Fir milenge. (See you again.)", "Kal milenge. (See you tomorrow.)",
            "Dur le  jao. (Take away.)", "Bhagwan ka shukr hai! (Thank God!)", "Sammaan dene ke liye dhanyawad. (Thanks for the honour.)",
            "Ye bahut hai. (This is much.)", "Ye bahut zyaada hai. (This is too much.)", "Bahar intezar karo. (Wait outside please.)",
            "Bachche carrom khelte hai. (Children play carrom.)", "Kya tum use pahchante ho? (Do you recognize him ?)", "Tum kaise jaoge? (How will you go?)",
            "Ham taxi kar lenge. (We will hire a taxi.)", "Kya bat hai? (What is the matter?)", "Taiyar ho jao. (Get ready/Be ready.)", "Tum kab free ho jaoge? (When will you become free?)",
            "Good afternoon", "Good evening", "shubh raatri (good night)", "Kya kar rahe ho.(what are you doing)", "Kya kar rahi ho.(what are you doing)",
            "Veh mera door ka rishtedaar hai. (He is my distant relative)", "Mujhe neend aa rahi hai. (I am feeling sleepy.)",
            "Tum kaun si film dekna pasand karogi ?  (Which movie would you like to watch?)", "Tumhen kon sa gana sabse zyada pasand hai?(Which movie would you like to watch?)",
            "Mere bhai ko apna kaam pura karana hai. (My brother has to complete his work.)",
            "Meri mataji ko khaana banana hai.  (My mother has to cook the food.)", "Ram kab padhta hai ?  (When does Ram study?)",
            "Tumne seeta ko kab dekha?  (When did you see Seeta?)", "Mai kal Apko fone karunga.  (I will call you tomorrow.)",
            "Is baat ko apne tak hi rakhna.  (Keep it up to yourself.)", "Mujhe kapde dhone hain.  (I have to wash the clothes.)",
            "Apka bhai kahan kam karta hai?  (Where does your brother work?)", "Apne ye pustak kahan se li?   (Where did you take this book from?)",
            "Kya jhagada hai? (What’s the conflict? / What’s the dispute?)", "Vo abhi abhi gaya hai.  (He has just gone/left.)",
            "Tum aisi bakwas kyu karte ho? (Why do you talk such nonsense?)", "Aisi baat nahi hai. (It is nothing like that. / Nothing as such.)",
            "Main nahi manta. (I don’t agree. / I don’t believe.)", "Use bukhar hai. (He has fever. / He is suffering from fever.)",
            "Hamare paas do dukanen hain. (We have two shops.)", "Aaj tum school kyu nahi gaye? (Why did you not go to school today?)",
            "Kya hua? (What happened? / What’s the matter?)", "Zara mai Taiyaar Ho lu. (Let me get ready.)", "Ghabrane ki baat nahi hai.(Nothing to worry about.)",
            "Tumhare paas bahut sadiyan hain. (You have many sarees.)", "Hamen khana banana pada tha.(We had to cook the food.)",
            "Roshni ko geet gaana pada tha. (Roshni had to sing a song.)", "Pitaji ko Delhi jana pada tha. (Father had to go to Delhi.)",
            "Main bhi sath chalun? (May I accompany you? / Shall I accompany you?)", "Kya shyam aa raha hai? (Is Shyam coming?)",
            "Halanki mai soch rha tha. (Though, I was thinking.)", "Isse behtar kuchh ho nhi sakta. (Nothing could be better than this.)",
            "Hamen scooter thik karna pada tha. (We had to repair the scooter.) ", "Aapko turant yahan aana pada tha. (You had to come here immediately.)",
            "Samajh gaye? (Understood? / Got it?)", "Kya ram andar hai ? (Is Ram in?)", "Kuch cheejen humare bas me nahi hoti. (Few things are not in our control.)",
            "Use mehanat karni padegi. (He will have to work hard.)", "Use mafi mangni padegi. (He will have to say sorry.)", "Mera bhai padh raha hai.(My brother is studying.)",
            "Ve bazar ja rahe hain. (They are going to market.)", "Aap kab aaye? (When did you come?)", "Mera ek kam karoge? (Would you do me a favor please?)",
            "Maine tumhare saath bahut bura kiya. (I really did wrong to you.)", "Aakhir chal kya raha hai. (What exactly is going on?",
            "Computer chalu karen kya? (Do/shall we switch on the computer?)", "Kya tumhen pata hai? (Do you know?)",
            "Tum kaayer ho. (You are coward.) ", "Ab vo kaam se bhaag nahi sakta. (He can’t shirk the work now.)", "Ham nashta kar rahe hain. (We are taking breakfast.)",
            "Vah Naha raha hai. (He is taking a bath.)", "Aap mujhse naraaz hain kya ?", "Are you annoyed with me? (Bataiye main aapki kya seva kr sakta hu?)",
            "Tell me, how can I help you?", "Yeh kameez maili hai. (This shirt is dirty.)",
            "Bobby abhi aaya hai. (Bobby has just arrived.)", "Usne tumhen ek gift bheja hai. (He has sent you a gift.)", "Yah kiska Mobile Number hai ? (Whose mobile number is this?",
            "Tum kab miloge ? (When will you meet?)", "Veh tumhare jesi dikhti hai. (She looks like you.)",
            "Kisi se kuch mat managna. (Don’t ask anything from anybody. / Don’t ask anybody for anything.)", "Ham cricket khel chuke hain. (We have played cricket.)",
            "Vah apni ichhayen puri kar chuka hai. (He has fulfilled his desires.)", "Ab apke pitaji kaise hain? (How is your father now?)",
            "Sabse achchhi shart kon si hai? (Which Shirt is the best?)", "Tumne mujhe jagaya kyo nahi? (Why didn’t you wake me up?)",
            "Mere pass 20 rupaye kam hain. (I am short by twenty rupees.)", "Main raat mein der se soya tha. (I slept late last night.)",
            "Tumane hamesha mujhe danta (You always scolded me.)", "Yah shurt kitne ki hai ? (What is the cost of this shirt?)",
            "Kitne din lagenge? (How long will it take?)", "Khelte hue tang par chot lag gayi. (I hurt my leg while playing.)",
            "Uski naak beh rahi hai. (His nose is running.)", "Binita ne madhur geet gaya. (Binita sang a sweet song.)", "Uska kal accident ho gaya. (He met with an accident yesterday.)",
            "Main aapse kuchh puchh raha hun. (I am asking you something.)", "kahne ki zarurat nahi ki vo pagal hai. (Needless to say that he is mad.)",
            "puchne ki zarurat nahi, vo pahle se jaanta hai. (Needless to ask, he already knows.)",
            "ye laabhdayak nahi hai. (It’s not worthwhile.)", "kya vahan jaana faaydemand hai? (Is it worthwhile going there?) ",
            "Ram bahut der baad aaya. (Ram came after a long.)", "Vo bahut der baad aaya tha. (He had come after a long.)",
            "maine chain kee saans lee.(I sighed of relief)", "Tum ab chain kee saans le sakte ho. (Now you can take a sigh of relief.)",
            "Tum mujhe Ram ki yaad dilate ho. (You remind me of Ram.)", "Tumhara chehra mujhe kisi ki yaad dilata hai. (Your face reminds me of someone.)",
            "Mere mama ne tumhe paala (My maternal uncle brought you up.)", "Main delhi me palaa badhaa. (I was brought up in Delhi.)",
            "Usne socha or sach kar diya. (He thought and materialized.)", "Main sapne saakaar kar dunga. (I will materialize the dreams.)",
            "Main hamesha tumhara saath deta hu. (I always stand by you.)", "Usne mera saath diya tha. (He had stood by me. (Vo hakla raha tha.)", "Rahul haklaata hai. (Rahul stammers.)",
            "Kya aap meri baat sun rahe ho? (Are you listening to me?)", "Mai kya-kya kha sakata hun? (What all can I eat?)",
            "Aajkal kaam bahut hai. (There is huge work pressure these days.)", "Ladke chay pee rahe the. (The boys were taking tea.)",
            "Main tumhara intejar kar rahi thi (I was waiting for you.)", "Yahi Mobile main chahata tha. (This is the very mobile, I want.)",
            "Maine pakka man bana liya hai ki mai jaunga. (I’ve decided to go.)", "Wah aaj kal mauj mai hai. (He is enjoying these days.)",
            "Tum table saaf kar rahe the. (You were cleaning the table.)", "Mere mitra doshi the. (My friends were guilty.)",
            "Hamne shimla me khub aanand liya. (We enjoyed a lot in Shimla.)", "Tumhare rukhe vyavhar se use chot  pahunchi hai. (Your rude behavior has hurt him.)",
            "Main aaj hi naukri par aaya hun. (I have joined today only.)", "In sab files ko lekar aao. (Bring all these files.)",
            "Main kal bajar jaunga. (I will go to market tomorrow.)", "Chaprasi kamre ki safai karega. (The peon will clean the room.)", "Ye kitaab kisaki hai ? (Whose is this book?)",
            "Apse ye ummid nahi thi.I didn’t expect it from you.", "Apka T.V band hai (Your TV is off.)", "Ham raat ko soyenge. (We will sleep at night.)",
            "Main aaj yah pustak padhunga. (I will read this book today.)", "Darvaje pe kaun khada hai? (Who is standing at the door?)",
            "Mai tum par bharosa kaise kar sakta hun ? (How can I trust you?)", "Hum rasta bhul gaye. (We have lost our way.)", "Yatra karte samaye kam saman lekar jana chahiye. (You should travel light.)",
            "Vah kal school ja raha hoga. (He will be going to school tomorrow.)", "Vah aaj aa raha hoga. (He would be coming today.)",
            "Glass mein thoda paani aur dalo. (Pour some more water into the glass.)", "Patr daak dvara bheja gaya. (Letter was sent by post.)",
            "Motor cycle ka agla pahiya panchar hai. (The front tyre of the bike is punctured/flat.)", "Apko pata hai kal mujhe Riya mili thi. (You know what I met Riya yesterday.)",
            "Apne kamre mein jao or intzaar karo. (Go to your room and wait there.)",
            "Kripya mere liye ek gilas paani laiye. (Please bring a glass of water for me.)", "Shyam tumhare peechhe khada tha. (Shyam was standing behind you.)",
            "Kalam mej par hai. (Pen is on the table.)", "Mujhe kisi tarah pata chal gaya. (I somehow got to know about it.)",
            "Main kuch keh rahi hun. (I’m saying something.)", "Apne dant achchhi tarah saaf Karen. (Clean your teeth well.)", "Apne svaasth ka dhyan rakhen. (Take care of your health.)",
            "Kitaab takie ke niche hai. (Book is beneath/underneath the pillow.)", "Mere pita ki jagah koi nahi le sakata. (Nobody can replace my father.)",
            "Tumne use kya bataya? (What did you tell him?) ", "Kya tumne kuch kaha? (Did you say something?)", "Das bajkar panch minat huye hain. (It is five past ten.)",
            "Meri ghadi tez chal rahi hai. (My watch is running fast.)", "Khud ke lie to har koi karata hai. (Obviously, everybody does for himself.)",
            "Tum mujhe naraz nahin ho na? (You are not angry with me, are you?)", "Tum kahan kuch kehti ho. (You hardly ever speak.)",
            "Kahin ghoomne jaane ka man kar raha hai. (I feel like going somewhere.)", "Achchha samay aayega. (Better time will come.)",
            "Baadal garaz rahe hain. (Clouds are thundering.)", "Aaj ke baad vo tumhen kabhi chot nahin pahunchayega. (Now onwards, he will never hurt you.)",
            "Main tumhen jane nahin dunga. (I will not let you go.)", "Man to kiya use ek thappad laga don. (I felt like slapping him.)",
            "Tumne abhi kiska naam liya? (Whose name did you take?)", "Main ek ghanta padhta hun. (I study for an hour.)",
            "Vah apna samay vyarth karta hai.He vastes his time. (Tumhen aisa nahi kahna chahie tha.)", "You shouldn’t have said so.(Main jana to chahta hu par ja nahi sakta.)",
            "I want to go but I can’t. (Din pratidin garmi badh rahi hai.)", "It’s getting hotter day by day. (Aap bilkul samay par aaye hain.)",
            "Batao, mujhe kya karna chaahie? (Tell me, what should I do?)", "Vo Delhi se abhi-2 aaya hai. (He has just come from Delhi.)",
            "Kya Ram achchha hai? (Is Ram good?) ", "Tum achchhe ho. (You are good.)", "Ye ladka kahan par tha? (Where was this boy?)", "Main Ram ki vajah se yahan hoon.(I am here because of Ram.)",
            "Tumhare pass kya hai? (What do you have?)", "Mammi ke pass paise nahi hai. (Mom doesn’t have money.)",
            "Ye aadmi kis ladki ka papa hai? (Which girl’s father is this man?)", "Main tumse lamba hoon. (I am taller than you.)",
            "Tum kis shahar se ho? (Which city are you from?)", "Tum kis shahar mein ho? (Which city are you in?)",
            "Kya tumhare paas hai? (Do you have?)", "Tumhare paas kya nahi hai? (What do you not have?)", "Kya tumhare paas mobile hai? (Do you have a mobile?)",
            "Main kaun hoon? (Who am I?)", "Kya hai vo? (What is that?)",
            "Mere peechhe kaun khada tha? (Who was standing behind me?)", "Main class mein baitha hoon. (I am sitting in the class.)",
            "Uske bhai kitne bade hain? (How old are his brothers?)", "Tum mere sabse chhote bhai ho. (You are my youngest brother.)",
            "Ye tumhare liye mera pyaar hai. (This is my love for you.)", "Kya hai uska naam? (What is his name?)", "Ye kahani kisi aur ki hai. (This story is someone else’s.)",
            "Vo thakee hui thi. (She was tired.)", "Ram soya hua hai. (Ram is asleep.)", "Hum baithe hue the (We were sitting.)",
            "Tum khade kyun ho? (Why are you standing?)", "Us table par kya hai? (What is there on that table?)",
            "Tum Delhi ke aas paas ho. (You are near about Delhi.)", "Main is photo mein nahi hoon. (I am not there in this photograph.)",
            "Vo kab se office mein hai? (Since when is he there in office?)", "Tum kab tak office mein the? (Until when were you there in office?)",
            "Mere pair mein kya hai? (What is there in my leg?) ", "Uske paas kuch nahi hai. (He doesn’t have anything.)", "Mere paas kuch phate hue kapde hain.I have some torn clothes.",
            "Ye bachche mere hai. (These children are mine.)", "Yah tu(mhara nahi hai. (This is not yours.)",
            "Ye meri billi hai. (his is my cat.)", "Ye billi meri hai. (This cat is mine.)", "Hum tumhare hain.(We are yours.)",
            "Main har pal tumhare sath tha. (I was there with you every moment.)", "Gadi me kitna petrol hai? (How much petrol is there in the car?)", "Tumhare pass kitna paisa hai? (How much money do you have?)",
            "kis shahar me ho tum is vakt? (Which city are you in right now?)", "Mai sirf tumhare liye zinda hoon. (I am alive only for you.)", "Main tumhare sapno me khoya hua hoon. (I am lost in your dreams.)",
            "Mobile table par rakha hua hai. (Mobile is kept on the table.)", "Mai dara hua tha. (I was scared.)", "Kitne bachche is samay yahan hai? (How many children are here at this time?)",
            "Uske papa piye hue the. (His father was drunk.)", "Tum mere kareebi dost ho. (You are my close friend.)", "Ye likha hua tha. (It was written.)",
            "Ye geeta me likha hua hai.(It is written in the Bhagavad Geeta.)", "Bhikhari ke kapde phate hue the. (The beggar’s clothes were torn.)",
            "Kya tumhare pass kuch tha? (Did you have something?)", "Hamare paas kuch hai. (We have something.)", "Main kiske liye vaha tha? (For whom was I there?)",
            "Tum ghar ke andar the. (You were inside the house.)", "Shiv kee pooja yahan prasiddh hai. (The worship of Lord Shiva is famous here.)",
            "Main zimmedariyon se kabhi jee nahi churata. (Main zimmedariyon se kabhi jee nahi churata.)", "Main kaam se bhaag nahi raha hu. (I am not shirking the work.)",
            "Vipatti insan ke dhairya ko parakhti hai. (Adversity tries one’s patience.)", "Aj mera tumse milne ka irada hai. (I intend to meet you today.)",
            "Vo tumhe dhokha dene ki sochta hai. (He intends to cheat you.)", "Is road ne hamari journey ko chhota kar diya. (This road shortened our journey.)",
            "Is lakdi ko chhota kar do. (Shorten this stick.)", "Main iska aadi nahi hona chahta. (I don’t want to be addicted to it.)", "Wo cigarette peene ka aadi ho gaya hai. ( (He is addicted to smoking.)",
            "Kya khushboo hai! (What a fragrance!)", "Is phool ki khushboo bahut achchhi hai. (The fragrance of this flower is very nice.)",
            "Hamari khvahishein itni kyon hai? (Why are our desires these many?)", "Tum aise kyon ho? (Why are you so?)", "Paise kiske paas hai? (Who has money?)",
            "kya tumhare pass dimaag nahi hai? (Do you not have brain?)", "Ye toota hua dil mera hai. (This broken heart is mine.)", "Uske kitne ladke hai? (How many sons does he have?)",
            "Aap mujhse jyada bure hain. (You are worse than I.)", "kya Ram mujhse jyada acha hai? (Is Ram better than me?)",
            "Mujhe tumse pyaar hai. (I am in love with you.)", "Itne sare paise tumhare paas kaise hai? (How do you have this much money?)", "Rishte khoobsurat hote hai. (Relations are beautiful.)", "Ye koi aur hai. (This is someone else.)",
            "Ye kuch aur hai. (This is something else.)", "Paise kiske pass nahi hain? (Who doesn’t have money?)", "Tum kis baat ke liye dukhi ho? (What are you sad for?)", "Main tumhare saamne khada tha. (Main tumhare saamne khada tha.)",
            "Main theek tumhare saamane khada tha. (I was standing just in front of you.)", "Kis ladki ke papa wahaan khade the? (Which girl’s father was standing there?)",
            "Main guitaar ke liye pagal hoon. (I am crazy for guitar.)", "Vahan kitne log hai? (How many people are there?)", "Samay kya hua hai? (What is the time?)",
            "Tum dono sabse ache ho. (You both are the best.)", "Hum sab tumhare saath hain. (We all are with you.)", "Main tumhara kaun hoon? (Who am I to you?)",
            "Mere saath raho. (Be with me.)", "Apne dosto se baat karo. (Talk to your friends.)", "Kisi dost ko mat chhodo. (Don’t leave any friend.)",
            "Mere baare mein socho. (Think about me.)", "Mujhe tumhe kuch batane do. (Let me tell you something.)", "Is pareshanee ka hal nikalo. (Find out the solution of this problem.)", "Mujhe dekhne do. (Let me see.)",
            "Naak saaf karo. (Blow your nose.)", "Kabhi-kabhi ghar aaya karo. (Drop in home sometimes.)", "Use ek inch bhi mat hilne do. (Don’t let him move even an inch.)", "Hamesha samay ke paaband raho. (Always be punctual.)",
            "Aise chalaak aadmee se saavdhaan raho. (Beware of such a clever man.)", "Apni kameej ke button band karo. (Button up your shirt.)",
            "Kripya darvaaje kee kundee laga do. (Kindly bolt the door.)", "Kaam se jee mat churao. (Don’t shirk the work.)", "Use dhundhne do. (Let him find out)", "Use pareshaan mat karo. (Don’t bother him.)",
            "Apni car yaha khadi mat karo. (Don’t park your car here.)",
            "Use ye mat karne do. (Don’t let him do this.)", "Apne mata-pita ko dekhne jao. (Go to see your parents.)", "Us ladke ke saath thahro. (Stay with that boy.)", "Gareebo ki madad karo. (Help the poor.)", "Is ladki ko yaha kaam karne do. (Let this girl work here.)",
            "Sabhi kapde press(istree) kar do. (Iron all the clothes.)", "Jmmedariyo se ji mat churao. (Don’t shirk responsibilities.)", "Baal bana lo. (Comb the hair.)", "Thoda pani aur mila lo. (Add a little more water.)", "Samay dekho. (Look at the time.)", "Yahaan mat utro. (Don’t get off here.)",
            "Hamein padhne do kyonki kal hamara paper hai. (Let us study as it is our paper tomorrow.)", "Mombattee bujha do. (Blow out the candle.)",
            "Sabko pyar karo jo koi tumharee jindagi mein aaye. (Love everyone whoever comes in your life.)", "Apna hisaab kar lo. (Clear your accounts.)",
            "Yahaan se chale jao. (Go away from here.)", "Is car se bahar nikal jao. (Get out of this car.)", "Khach-pach mat likho, saaf-saaf likho. (Do not scribble, write legibly.)", "Jaldee theek ho jaiyee. (Get well soon.)", "Mudde par aaiye. (Come to the point.)", "Apne vade se mat mukro. (Do not back out of your promise.)", "Farsh par yaha vaha mat thuko. (Don’t spit on the floor here and there.)", "Khoob muskurao. (Smile a lot.)",
            "Jaldi office pahucho kyunki boss naraaz hai. (Reach office early as boss is angry.)", "Zyada mat khao. (Do not overeat.)", "Apne joote nikaal lo. (Take off your shoes.)", "Faltu baat mat karo. (Do not talk nonsense.)", "Ek nal tha. (There was a tap.)", "Kya tumhara bhai wahaan tha? (Was your brother there?)", "Koi hai kya? (Is someone there?)", "Kya aapke dil mein pyaar nahi hai? (Isn’t there love in your heart?)",
            "Tum wahaan kyon chipe hue ho? (Why are you hidden there?)", "Jungle mein ek raja rehta tha. (There lived a king in Jungle.)", "Wahaan kuch nahi tha. (Nothing was there.)", "Ek raja wahaan gaya. (There went a king.)", "Wahaan kya hai? (What is thee?)", "Us shahar mein ek park tha. (There was a park in that city.)",
            "Kya tumhare pass paise nahi hai? (Do you not have money?)", "Class mein 3 ladkiyaan baithee thee. (There were 3 girls sitting in the class.)",
            "Mere papa wahaan rehte the. (There lived my dad.)", "Table par pen tha. (There was a pen on the table.)", "Takiye ke neeche ek patr hai. ) (There is a letter beneath)", "Vahan koi nahihoga. (There will be no one.)", "Tumhare bhai ke saath ek aadmee khada hai. (There is a man standing with your brother.)",
            "Is tarah ke kai phool hai. (There are so many such flowers.)", "Vahan dekhne ko kuch nahi hai. (There is nothing to see.)",
            "Khelne ke liye ladke nahi hai. (There are no boys to play.)", "Uske batue mein paise nahi hai. (There is no money in his wallet.)",
            "Is company mein aage badhne ke avsar hain. (There’re opportunities in this company to grow.)", "Jaane ki zaroorat nahi hai. (There is no need to go.)",
            "Burger khane ki koi jaroorat nahi. (There is no need to eat burger.)", "Ek saanp tha. (There was a snake.)",
            "Ek ped hai, jiska rang laal hai. (There is a tree of red colour.)", "Us kuye mein pani nahi tha. (There was no water in that well.)", "Jab kbhi mai pareshan tha, tum mere sath the. (Whenever I was in trouble, you’re there with me.)", "Maine jo kuch kiya, vahi par kiya. (Whatever I did, I did there.)", "Uski zindagi mein khushi nahi hai. (There is no joy in his life.)", "Kya vahan kuch chal raha hai? (Is something going on there?)", "Pyaar jaisi koi cheez nahi hoti. (There is no such thing as love.)",
            "Aisa koi shabd nahi hota. (There is no such word.)", " Aisi koi kahani nahi hai. (There is no such story.)", "Aisa koi gaanv nahi hai. (There is no such village.)", "Aisa koi desh nahi hai, jahan sirf janvar rahte ho. (There’s no such a country, where there’re only animals.)",
            "Aisa koi mobile nahi h, jise mai thik nahi kr sakta. (There is no such a mobile, which I can’t repair.)", "Kya vahan koi nahi hai? (Isn’t there anyone?)",
            "Aisa koi aadmee nahi hota jise chot nahi lagti. (There is no such a man, who doesn’t get hurt.)", "Wahaan kitne log hai? (How many people are there?)",
            "Wahaan kitne log the? (How many people were there?)", "Wahaan kya hai? (What is there?)", "Tum vahan kiske saath the? (With whom were you there?)",
            "Us gaon mein bijlee nahi hai. (There is no electricity in that village.)", "Kaan kholkar sun lo. (You better listen up.)", "Bahut saha, bas ab nahi! (Suffered a lot, but not anymore! (Mujhe paune saat baje uthna hai.)", "I am to get up at quarter to seven. (Ham bahut shighr aa gaye hain.)",
            "Hamen safalta ka pura bharosa hai. (We are sure of success.)", "Thanda pani mat piyo. (Don’t take chilled water.)", "Thandi cheeje mat khao. (Don’t take chilled eatables.)", "Vah 8 baje uthta hai. (He wakes up at 8 o’clock.)", "Vah samay ka poora paaband hai. (He is quite punctual.)",
            "Bijli bhi chamak rahi hai. (It is lightning.", "Mujhe aapka patra mila par main padh nahi saka. (I got your letter but I couldn’t read.)",
            "Jab aap aaye, vo nikal chuka tha. (When you came, he had left.)", "Theek hone ke bad ice cream kha lena. (Take ice-cream after you get well.)",
            "Meri ghadi band ho gai hai. (My watch is not functioning.)", "Vah kaamp raha hai. (He is shivering.)", "Main janta hun, ap mujhse naraaz ho. (I know you are not happy with me.)", "Vah gitaar ka aadi ho gaya hai. (He has been addicted to guitar.)", "Tumhe to khansi ho gayi hai. (You have got cough.)", "Muh dhak kar khansi karo.(Cover your mouth when you cough.)", "Sava teen baje hain. (It’s quarter past three.)",
            "Apni adaten sudharo. (Mend your ways.)", "Tum din raat unnati karo, yahi meri dua hai! (May God succeed you by leaps and bounds!)",
            "Yah patr budhavar tak pahunch jaana chaahie. (This letter must reach before Wednesday.)", "Dikhao chot kahaan lagi hai? (Show me, where you’ve been hurt?)",
            "Is hafte iska teekakaran karvana hai. (His vaccination should be done in this week.)", "Seedhe khade raho, jhuko nahin. (Stand upright, don’t bend.)",
            "Main seekhne me apna samay bitaata hu. (I spend my time in learning.)", "Use pyar ka matlab nahi pata. (He doesn’t know the meaning of love.)",
            "Aaj budhar hai, vo ravivar ko aaega.(It’s Wednesday today, he’ll come on Sunday.)", "Jitni bhook ho utna hi khana. (Eat as per your appetite.)",
            "Bed par khana mat girao. (Don’t spill the food on the bed.)", "Main exercise ke lie samay nahin nikal pa raha hun. (I am not able to make time for exercise.)",
            "Jitana ho sake saaf likho. (Write as legibly as you can.)", "Ye mera saubhagya hai ki main tera dost hun. (It’s my pleasure to be your friend.)",
            "Aapse baat karke bahut achchha laga. (It was pleasure talking to you.)", "Zid mat karo.(Don’t be stubborn.)", "Tum bhut ziddi ho gaye ho. (You have become very obstinate.)", "Bhad mein jao. (Go to hell.)", "Aavesh mein na aao. (Don’t get excited.)", "Main kaam mein laga hua tha. (I was into some work.)", "Tumhen uska zara bhi khyaal nahin aaya? (You didn’t even think about him?)", "Baalo mai tel laga lo. (Apply oil on your hair.)",
            "Aaj aap kis ke sath soyenge? (With whom will you sleep today?)", "Main to mazaak kar raha tha. (I was just kidding.)", "Maaf kijie, main samay par nahin aa saka. (I am sorry, I got late.)", "Mujhe bahut baten karni hai. (I have a lot to talk about.)", "Iske zimmedar tum ho. (You are responsible for this.)",
            "Aapko huyi asuvidha ke liye hame khed h. (We regret for the inconvenience caused to you.)", "Main tumse zarur milunga. (I’ll surely meet you.)",
            "Atak atak ke mat bolo. (Don’t stutter.)", "Jaldbaji mai tum bhool jate ho. (You always forget in haste.)", "Sabse pahla mahina kaun sa hai? (Which is the first month?) ", "Ham sabko angreji bhasha sikhani chahie. (We all must learn English language.)", "Uski pratibha kabiletarifh hai. (His talent is praiseworthy.)", "Vah hume chup chup kar dekh rahi hai.(She is sneaking out at us.)", "Tumne mujhe aaj kese call kiya? (How did you call me up today?)",
            "Bharat gantantr kab bana? (When did India become Republic?)", "Aap kab paida huye? (When were you born?)", "Ab mujhe chalna chahie. (I must leave now.)",
            "Jab tak tum mehnat nahi karoge, safal nahi ho paoge. (Unless you work hard, you’ll not be able to succeed.)", "Kya aap usse meri baat kara denge? (Could you please make me talk to him?)", "Apki aawz kat rahi hai.(I’m sorry, you are breaking up.)", "Kya aapne abhi ticket nahin liya hai (Haven’t you bought the ticket so far?)", "Sadak par marammat chal rahi hai.(The road is under repairing.)", "Jab tab aap nahin aaoge, main intazar karunga. (Until you come, I’ll wait.)", "Vo itna bimar h ki bistar se uth nahi sakta. (He is too weak to rise from the bed.)", "Phone ko charging se utaar do. (Unplug the phone from charging.)", "Tumne kisko cycle ke peeche bithaya hai? (Whom have you made sit at the back of your bicycle?)",
            "Yah bus kahaan jati hai? (Where does this bus go?)", "Abhi gadi aane me aadha ghanta baki hai. (It is still half an hour for the train to arrive.)",
            "Jaise hi maine khelna shuru kiya, papa aa gaye. (The moment I started playing, dad turned up.)", "Usne aisa kya kaha ki tumhen bura lag gaya? (What did he say to make you feel bad?)", "Ye paisa vasool movie thi.(This movie was worth watching.)", "Train kis samay chootti hai?(When does the train leave?)",
            "Uski jaan mein jaan aayi. (He heaved a sigh of relief.)", "Yah bachcha bada pyara lagta hai. (This child looks very lovely.)",
            "Mera tumhen dukh pahunchane ka koi Irada nahi hai. (I don’t mean to hurt you.)", "Bahut si nadiyon mein badh aai hui hai. (Many a river is flooded.)",
            "Usne mujhe saaf mana kar diya. (He gave me a flat refusal.)", "Aap logon ke sahayog ke lie dhanyavad. (Thanks for your cooperation.)",
            "Train do ghante deri se hai. (The train is late by two hours.)", "Prateeksha grah mein chalte hain.(Let’s go to the waiting room.)",
            "Tumne saara khel bigad diya. (You spoiled the whole game.)", "Aapne mere man ki baat kah di. (You spoke out my mind.)", "Main unka ye sapna zarur pura karunga. (I’ll surely make his dream come true.)", "Train aane hi wali hai. (The train is about/likely to arrive.)", "Train par chado. (Get in the train.)",
            "Sahi samay par prayas karo. (Strike the iron when it is hot.)", "Aaj kal svarth ka jamana hai. (Selfishness is common these days.)",
            "Main bazar gaya tha, raju mujhe mila tha. (I met Raju in market.)", "Mujhe apne aap par bharosa hai. (I believe in myself.)",
            "Samaan ko bagal mein rakho. (Keep the luggage beside.)", "Kuch khane ke liye lelo. (Get something to eat.)", "Yah mere bas ki baat nahin. (It is beyond my capacity.)", "Aapka yahan kaise aana hua? (What brings you here?)", "Mai tumhe maar dalunga”,usne aisa kaha. (I’ll kill you”, he said so.)",
            "Tumne use jaane kyo diya?(Why did you let him go?)", "Train kab chalegi? (When will the train leave?)", "Main duvidha mein hun ki kya karun? (I am in a dilemma what to do?)", "Tumne mujhe ek rupya kam diya hai. (You have given me a rupee less.)", "Tum kuchh bhi kro, mujhe fark nahi padta. (You do anything, I don’t care.)", "Koi fark nahi padta. (It doesn’t make any difference.)", "Agla station Dehradun hai. (The next station is Dehradun.)",
            "Apka station aa gya. (Here is your station.)", "Dono ek dusre ke bahut kareebi hai(Both are close to each other.)", "I’ll go Shimla next month. (Vah hmesha mere raaste me tang adata h.)", "Train chalne mai kuch der hai (There is still some time in train’s departure.)", "Hame kadi se kadi mehnat karni hai. (We have to work harder than the hardest.)", "Aapke bhai ko kab se bukhar hai?(How long has your brother been down with fever?)", "Main use dhokha nahi de sakta. (I can’t cheat her.)", "Usne mujhe dhokha diya.(He cheated on me.)", "Kya mujhe kafi der intejaar karna padega?(Will I have to wait for a long?)",
            "Mujhe daant ke Doctor se milna hai. (I want to see a dentist.)", "Kal raat mujhe bukhar ho gaya tha. (I had a fever last night.)",
            "Ham taxi kar lenge. (We will hire a taxi/cab.)", "Mujhe gahre rang ka frem chahie. (I need a darker frame.)", "Mere paise ek saal me khatm ho jaayenge. (My money would run out in a year.)", "Samay bahut jaldi beet jaata hai.(Time runs out very quickly.)", "Uska ek aalishaan farm house hai. (He has a sumptuous farm house.)", "Ye ek behtareen breakfast hai. (This is a sumptuous breakfast.)", "Apne pariwar ka dhyan rakho. (Take care of your family.)",
            "Niyamon ka paalan karo. (Abide by the rules.)", "Aapne ye kitne achchhe se bataya! (How elegant you explained it!)", "Yahaan tambaaku ki kheti hoti hai (Tobacco is cultivated here.)", "Kuch bhi aisa nahi hai jo khatm na ho. (Nothing is immortal.)", "Vo ek dhokhebaaz vyakti hai. (He is a fraudulent person.)",
            "Aapke kaam mein koi galati nahin hai. (There is no flaw in your work.)", "Aap mujhe uske khilaf mat bhadkayiye. (Don’t provoke me against him.)",
            "Uski aankho se aansoo tapak rahe the. (The tears were trickling from his eyes.)", "Maine nal se paani tapakne ki aawaaz suni. (I heard the trickle of water from the tap.)",
            "Tum uske pyar ko paise se nahi tol sakte. (You can’t assess his love for money.)", "Ise aasani se khatm nahi kiya ja sakta. (It can’t be eradicated easily.)",
            "Is mudde par aapka kya drishtikon hai?  (What’s your approach in this matter?)", "Main khelne ka shaukeen hu.  (I am fond of playing.)",
            "Wo guitar bajaane ka shaukeen hai. (He is fond of playing guitar.)", "Maine turant uska virodh kiya. (I retaliated immediately.)",
            "Aapko ye tatthya logo se nahi chhipana chahiye.  (You shouldn’t conceal this fact from public.)", "Hum satya ko zyada der tak chhipa nahi sakte.(We can’t conceal the truth for a long.)",
            "Uski baato ne mujhe himmat di. (His words strengthened me.)", "Is shahar me English kaafi boli jaati hai. (English speaking is prevalent in this city.)", "Yahaan cigarette peene wale kaafi hai. (Cigarette smoking is prevalent here.)",
            "Vo kaafi samay se yahaan hai. (He has been here for a long.)", "Is dawai ne mera dard kam kar diya. (This medicine lessened my pain.)",
            "Thodi der ke liye mere sath raho. (Stay with me for a while.)", "Usne bas thodi der kaam kiya. (He worked just for a while.)",
            "Mujhe uske baare me kal hi pata chala. (I got to know about her yesterday itself.)", "Apne bhaashan ko lamba mat kheencho. (Don’t elongate your speech.)", "Vo kisi bhee baat ko lamba karke batata hai. (He elongates his talks.)",
            "Vo mere peeche peeche fir raha hai. (He is running after me.)", "Mujhe uske baare me kal hi pata chala. (I came to know about her yesterday itself.)", "Maine contract nahi toda. (I didn’t breach the contract.)",
            "Nirashavadi mat baniye. Aashavadi baniye. (Don’t be pessimistic. Be optimistic.)", "Usne saari zameen apne adhikar me le li (He acquired the whole land.)", "Main aap par apni ichchha nahi thop sakta. (I can’t impose my will on you.)",
            "Usne match me bahut jabardast performance di. (He had an incredible performance in the match.)", "Usne mujhe good bye kiya or chala gaya. (He bade me good bye and left.)", "Maine ye insaniyat ke naate kiya hai (I have shown humanity.)",
            "Main use bas apna ek zariya banauga (I will just use him for my sake.)", "Ye sab usaki chal hai tumhe fasane ki (This is all his conspiracy to trap you.)", "Is jhande ko yahan gadna hai. (This flag is to be dug here.)", "Apne pairon ko ghasito mat. (Don’t drag your feet.)",
            "Ghutne ke bal mat chalo. (Don’t drag your knees.)", "Kursi ko ghasito mat. (Don’t drag the chair.)",
            "Pen ko ghasito mat. (Don’t drag the pen.)", "Apne nakhoon mat chabao. 9Don’t bite your nails.)", "Apni ungali munh mein mat dalo. (Don’t put your finger into your mouth.)", "Apne kapade badal lo. (Change your clothes.)", "Apna homework Complete karo. (Complete your homework.)",
            "Apni notebook ke pages ko mat phaado. (Don’t tear the pages of your notebook.)", "Halla mat karo. (Don’t make a noise.)", "Jhagda mat karo. (Don’t make a quarrel.)", "Kya aap ek jagah par nahin baith sakte? (Can’t you sit still at one place?)",
            "Sthir khade raho. hilna mat.Stand still. Don’t move. (Mobile par khelna band karo.)",
            "T.V. dekhna band karo. (Stop watching TV.)", "Cartoon dekhna band karo. (Stop watching cartoon.)", "Apne daant achchhe se saaph karo. (Brush your teeth properly.)", "Apne kapade pahan lo. Wear your clothes",
            "Apne joote pahan lo. (Put on your shoes.)", "Apne kapade utaar do. (Take off your cloths)", "Apne joote ke tasmein baandh lo. (Tie your shoe laces.)",
            "Apne joote utaar do. (Take off your shoes.)", "Apna khana jaldi khatm karo. (Finish your food quickly.)", "Ise mat chhedo. (Don’t touch it.)",
            "Mere mobile ko mat chhedo. (Don’t touch my mobile.)", "T. V. ko mat chhedo. (Don’t touch the TV.)", "Laptop ko mat chhedo. (Don’t touch the laptop.)",
            "Apni kitabein apne bag mein rakh do. (Keep your books in your bag.)", "Apne joote shoe rack mein rakh do. (Keep your shoes in the shoe rack.)",
            "Bilkul nahin! (Not at all!)", "Ek tukada bhee nahin!(Not even a bite!)", "Jaldbaaji mat karo. (Don’t be hasty.)", "Galat baat mat karo. (Don’t talk nonsense.)",
            "Main maje kar raha hun. (I am having fun.)", "Main bor ho raha hun. (I am feeling bored.)", "Main khana kha raha hun.(I am eating)",
            "Main office ke lie nikal raha hun.(I am leaving for office.)", "Main office mein nahin hun. (I am not in office.)", "Hichkichao mat. (Don’t hesitate.)",
            "Mujhe sach batao. (Tell me the truth.)", "Glass ko ulta kar do. (Turn the glass upside down)", "Nal khol do. (Turned on the tap.)", "Computer band kar do. (Turned off the computer.)",
            "Dil pe mat lo (Don’t take it to heart.)", "Wo raste mein hai. (He is on the way.)", "Wo prashansa ke laayak hai.(He is praiseworthy.)", "Wo ekadh din mein aayega. (He will come in a day or so.)", "Main ekaadh ghante me tumse miloonga. (I will meet in an hour or so.)",
            "Mission poora hua. (Mission accomplished.)", "Bilkul chinta mat karo. (Don’t worry at all.)", "Aaj taptapaati garmi hai. (It’s blistering heat today.)",
            "Tumhaare dil mein kuchh to hai. (There is something in your heart.)", "Aaj kanpkapaati thand hai. (It’s shivering cold today.)", "Tez bolo. (Speak aloud.)", "Mera intazaar karana. (Wait for me.)", "Isake baare mein socho. (Think about it.)", "Aap kise pyaar karte ho? 9Whom do you love?)",
            "Kitne ghammadi ho aap! (How arrogant you are!)", "Mujhe kal phon kariega. (Call me tomorrow.)", "Samajh aaya? (Got it? / understood?)", "Samajh aa gaya. (I got it. /I Understood.)", "Mujhe aisa nahin lagata. (I don’t think so.)", "Aaj kitni taarikh hai? (What is the date today?)",
            "Ye kiska hai? (Whose is this?)", "Bahut achchha! (Too good! / Very nice!)", "Bahut bura! (Too bad! / Very bad!)", "Bahut achchha hona bhi achchha nahin hota. (It’s not good to be too good.)", "Khud se pyaar karo. (Love yourself.)", "Ye paap hai. (It’s a sin.)", "Aap thake huwe lag rahe ho. (You are looking tired.)",
            "Samay kisi ka intazaar nahin karta. (Time waits for none.)", "Kya aapko ye chaahie. (Do you need it?)", "Kya aapko kuchh chaahie? (Do you need something?)",
            "Usi tarah ka kuchh. (Something of that sort.)", "Kuchh na kuchh. (Something or the other.)", "Kaheen na kaheen kuchh to galat hai. (Something is wrong somewhere.)",
            "Use meree jarurat pad sakatee hai. (He may/might/could need me.)", "Kuchh bhi vyaktigat nahin hai. (Nothing is personal.)", "Ye keval ek soch hai. (This is just a mindset / thinking.)", "Mujhe 8 baje yaad dila dena.(Remind me at 8.)", "Aap Apne umr se kam dikhate ho. (You look younger than your age.)",
            "Kuchh khaas nahin. (Nothing special!)", "Hamesha ki tarah. (As usual!)", "Khaali mat baitho. (Don’t sit idle.)", "Kuchh badhiya. (Something worthy!)",
            "Kuchh bhi jo tumhen achchha lage. (Anything, you feel like.)", "Main job dhundh raha hun. (I am searching a job.)", "Pata hai…. (You know what…)",
            "Ye mat karo. (Don’t do it.)", "Kya tum padhai kar rahe ho? (Are you studying?)", "Kya haal hai (How are you? /What’s up? /How are you doing?",
            "Pagal ho kya? (Are you mad or what?)", "Aapka din shubh ho! (Have a nice day)", "Aapki yaatra mangalmay ho. (Have a nice journey.)",
            "Doosra le lo. (Have another one.)", "Dhairya rakho. (Have patience.)", "Masti karo. (Have fun.)", "Mujhe jaane do. (Let me go.)",
            "Isse koi phark nahin padata. (It doesn’t matter.)", "Aapka matlab kya hai? (What do you mean?)", "Kitne matlabi ho aap! (How selfish you are!)",
            "Kitna bakavas hai! (How ridiculous!)", "Mujhe iski aadat hai.(I am used to it.)", "Zaahir si baat hai. (It’s obvious.)", "Kya ham dhongi hain? (Are we hypocrite?)", "Ye phayade ka sauda nahin hai. (It’s not worthwhile. / It’s not worth it.)", "Kisi ka dil mat dukhao. (Don’t hurt anyone.)",
            "Dost Banane se pahle kayi bar socho. (Think many a time before making friends.)", "Hamesha vinamra raho. (Always be polite.)", "Dayaloo bano. (Be generous.)", "Aapne mujhe hairan kar diya. (You amazed me.)", "Khana bahut svadisht tha. (The Food was delicious.)", "Khud par vishvaas karo.Believe in yourself.",
            "Jo hota hai, achchhe ke lie hota hai. (All happen for good.)", "ab aapaki bari hai. (It’s your turn now.)", "sach mein! (Really! / Is it!)", "kya hua tumhen? (What happened to you? /What’s wrong with you?",
            "aapka svagat hai (You are welcome.)", "aap jhuth bol rahe ho (You are lying.)",
            "aisa phir kabhi mat karna (Don’t ever do it again.)", "kya aap shyor ho? (Are you sure?)", "bilakul nahin (Not at all.)",
            "kitana bakvas hai! (How disgusting!)", "maine nahin kaha. (I didn’t say.)", "main use phone kyon karunga. (Why would I call him/her?)",
            "kya aap Delhi mein rahe ho? (Have you lived in Delhi? /Have you been in Delhi?)", "kya aap Delhi gaye ho? (Have you gone to Delhi?)",
            "kya karun main? (What do I do? / What shall I do?)", "aisa sochana bhi mat. (Don’t even think so.)", "bhool jao. (Forget it.)",
            "Mujh Par Bharosa Rakho. (Trust me.)", "mujhe vishvas nahi ho raha. (I can’t believe it.)", "thodi der mein phone karie. (Call me a bit later.)",
            "ye sab uski chaal hai tumhen fansane ki. (This is all his conspiracy to trap you.)", "maine kisi ka kya bigada hai? (What wrong have I done to anyone?)",
            "paani gunguna ho gaya hai. (The water has turned lukewarm.)", "aisa mere saath hi kyon hota hai? (Why does it happen only with me?)",
            "Kal raat boonda baandi hui. (It drizzled last night.)", "Samay ke sath chalna bada mushkil hai. (It is difficult to match pace with the time.)",
            "Aap kab aaye? (When did you come?)", "Main angreji bol sakta hun. (I can speak English.)", "Pitaji ke paas ek sundar pen hai. (Dad has a beautiful pen.)",
            "Aapke papa ka kya naam hai? (What is your father’s name?)", "Kya time hua hai? (What time is it?)", "Kya hua? (What happened?)",
            "Wahan kya kya hua? (What all happened there?)", "Uske sath kya kya hua? (What all happened with him?)", "Uske paas kya hai? (What does he have?)",
            "Uske paas kya kya hai? (What all does he have?)", "Aapke paas kya hai? (What do you have?)", "Aapke paas kya nahi hai? (What do you not have?)",
            "Aapne kya kharida? (What did you buy?)", "Kya chal raha hai? (What is going on? / What’s going on?)", "Mujhe nahi pata kya hai ye. (I don’t know what it is.)", "Aap kya karne ki koshish kar rahe ho? (What are you trying to do?)",
            "Akhiri ravivar ko kya kiya aapne? (What did you do last Sunday?)",
            "Akhiri ravivar ko kya kya kiya apne? (What all did you do last Sunday?) ", "Aapka matlab kya hai? (What do you mean?)", "Us ladke me tumhe kya pasand hai? (What do you like in that boy?)", "Mujhme tumhe kya pasand hai? (What do you like in me?)",
            "Kya insaan hain aap! (What a person you are!)", "Kya ladki hai vo! (What a girl she is!)",
            "Aap kya karte ho? (What do you do?)", "Agla topic kya tha? (What was the next topic?)", "Wo aaj kya perform kar rahe hain? (What are they performing today?)",
            "Apka pasandida khel kya hai? (What is your favorite sport?)", "Apki visheshta kya hai? (What’s your quality?)", "Mujhe dedo jo kuch bhi tumhare paas hai. (Give me whatever you have.)", "Jo kuch mainene chaha, mujhe vo mila. (Whatever I wanted, I got that.)",
            "Jo kuch bhi tumne kaha, galat hai. (Whatever you said, is wrong.)", "Jo kuch bhi aap choose karte ho, mera hai. (Whatever you choose, is mine.)", "Aap kahaan ho? (Where are you?)",
            "Mein Ayansh ke sath hu, jo mera beta hai. (I am with Ayansh, who is my son.)", "Apko kaunsi book chaiye? (Which book do you want?)",
            "Kaun si jagahein aapko sabse zyada pasand hain? (Which places do you like the most?)", "Tumhe dawai chahiye, par kaunsi? (You need a medicine, but which one?)", "Main ek kitab dhoond raha hu, jo ki laal colour ki hai. (I am looking for a book, which is in red colour.)",
            "Android or windows phones me tumhe zyada pasand  kaunsa hai. (Which one do you prefer, android phones or windows phones?)",
            "Shaadi ke liye aap kaunsa choose karogi? (Which one would you choose for wedding?)", "Kis college se ho aap? (Which college are you from?)",
            "Ye dibba khaali hai, jisme bahut saare phool the. (This box is empty, which had many flowers.)", "Aap/tum kaun ho? (Who are you??)",
            "Kaun jaanta hai? (Who knows?)", "Kaun kaun jaante hai? (Who all know?)", "Kisne kahaan? (Who said?)", "Is tasveer ko kisne paint kiya? (Who painted this picture?)", "Wo ladki kaun hai? (Who is that girl?)", "Wo ladka kaun hai? (Who is that boy?)",
            "Is selfie me ye kaun hai? (Who is this in this selfie?)", "Guitar kaun baja raha hai? 9Who is playing the guitar?)", "Apka favourite hero kaun hai? (Who is your favourite actor?)",
            "Apke favourite actors kaun kaun hai? (Who all are your favourite actors?)", "Mujhe nahi pata, cake kisne banaya? (I don’t know who made the cake.)",
            "Sawaal ye hai, kaun is project ko karega? (The question is who will do this project?)", "Aaj raat kaun gaane waala hai? (Who is going to sing tonight?)",
            "India ka pahla pradhanmantri  kaun tha? (Who was the first prime minister of India?)", "Is tasveer ko kaun bana sakta hai? (Who can make this picture?)",
            "Apki rai kya hai, kaun match jitega? (What is your opinion, who will win the match?)", "IT department mein Rahul kaun hai? (Who is Rahul in IT department?)",
            "Agla superstar kaun hoga? (Who will be the next superstar?)", "Kisko ice-cream chahiye? (Who wants ice-cream?)", "Kaun khana pakayega? (Who will cook the food?)", "Ye Ram hai, jo mere sath tha. (This is Ram, who was there with me.)",
            "Vo mera dost hai, jisne tumhe call kiya tha. (That is my friend, who had called you.)", "Jo koi mujhe jaanta hai, mujhse pyar karta hai. (Whoever knows me, loves me.)",
            "Jo koi bhi yaha hai, Rahul uske sath baat karna chahta hai. (Rahul wants to speak with whoever is here.)", "Tumne Kyo pucha? (Why did you ask?)",
            "Tum ander kyo nahi aate? (Why don’t you come in?)", "Usne job kyo chodi? (Why did he quit the job?)", "Tumne ye tennis table kyo kharidi? (Why did you buy this tennis table?)", "Tumne box open kyo kiya? (Why did you open the box?)", "Hum pizza order kyo na kare? (Why shall we not order pizza?)",
            "Tum gussa kyo ho? (Why are you angry?)", "Aaj tum itne thake hue kyo ho? (Aaj tum itne thake hue kyo ho?)", "Tum actor kyo banna chahte ho? (Why do you want to become an actor?)", "Aaj subah tum late kyo the? (Why were you late this morning?)", "Tum wahan se kyo bhaage? (Why did you run from there?)",
            "Tum niraash kyo ho? (Why are you disappointed?)", "Usne iske baare mein use kyo nahi kahaan? (Why didn’t she tell him about it?)", "Ve us building ke aas paas kyo ghoom rahe hai? (Why are they walking around that building?)",
            "Wo sab achchhe se jaante hain, Riya kyo dukhi hai.(They all know very well why Riya is upset.)", "Aaj tum itne late kyo ho? (Why are you so late today?)",
            "Ye pankha kaam kyo nahi kar raha? (Why is this fan not working?)", "Tum kab padhai karte ho? (When do you study?)", "Yeh kab shuru hota hai? (When does it start?)", "Tumne ye saari kab kharidi? (When did you buy this saree?)", "Tumhara birthday kab hai? (When is your birthday?)",
            "Tum wapas kab aa rahe ho? (When are you coming back?)", "Ravi baahar tha jab mene call kiya. (Ravi was out when I called.)", "Tumari pariksha kab hai? (When is your exam?)", "Hum kab pahuchenge? (When will we arrive?)", "Tumne phone kab change kiya? (When did you change your phone?)",
            "Jab tumhare paas samay ho, mujhe message karna. (Whenever you have time, message me.)", "Jab me jawan tha, to acche se tair skta tha. (When I was a young, I could swim well.)", "Jab mein ghar aaya, Shikha ro rahi thi. (When I came home, Shikha was crying.)",
            "Jab wo 20 saal ki thi, uski shaadi ho gayi. (When she was 20, she got married.)", "Me tumhe call karunga, jab hum jaayenge. (I’ll call you when we go.)",
            "Kajal usse kab milegi? (When will Kajal meet him?)", "Tum kab soye? (When did you sleep?)", "Payal kahaan thi, jab uske parents aaye? (Where was Payal, when her parents turned up?)", "Jab kabhi main aaunga, hum movie dekhne chalenge. (Whenever I come, we will go for a movie.)",
            "Maine apne dil ki baat usse kahi, jab kabhi mujhe mauka mila. (I spoke my heart to her, whenever I got a chance.)", "Aap kaise ho? (How are you?)",
            "Tumhara bhai kaisa hai. 9How is your brother?)", "Tumhari chutiyaan kaise rahi? (How were your holidays?)", "Aaj ka show kaisa tha? (How was today’s show?)", "Tumhare papa kitne saal ke hai? (How old is your father?)", "Tumhari car kitni lambi hai? (How long is your car?)",
            "Wo kaise jata hai? (How does he go?)", "Mujhe nahi pata veg biryani kaise banani hai. (I don’t know how to cook veg biryani.)",
            "Tumne is movie ko kitna enjoy kiya? (How much did you enjoy this movie?)", "Apke kitne bachche hai? (How many children do you have?)",
            "Uske kitne bachche hai? (How many children does he have?)", "Apke kitne bachche the? (How many children did you have?)",
            "Mere bete ko competitive exams ke liye kitna padhna chaiye? (How much should my son study for competitive exams?)", "Mobile per tum kitna time bitate ho? (How much time do you spend on mobile?)", "Tumne tabla bajana kaise sikha? 9How did you learn to play Tabla?)",
            "Is machine ko kaise use karte hai? (How to use this machine?)", "Is puzzle ko kaise solve kare? (How to solve this puzzle?)", "Ek mahine mein tum kitni books padhte ho? (How many books do you read in a month?)", "Tum dance kaise karoge, jab tumare pair kam nahi kar rahe hai? How will you dance, when your legs not working?)",
            "Dekho,Us tyohaar ko log kaise mana rahe hai! (See, how the people are celebrating that festival)", "Aap wahaan aksar kab kab jaate ho? (How often do you go there?)", "Aap kab kab usse milte ho? (How often do you meet him?)", "Yahaan se bus stop kitna door hai? (How far is the bus stop from here?)",
            "Aap kitni door jaa sakte ho? (How far can you go?)", "Tum kise invite karne wale ho? (Whom are you going to invite?)", "Usne is post ke liye kise hire kiya? (Whom did he hire for this post?)", "Tumne mera beg kise diya? (Whom did you give my beg)", "Tumne kisko dekha? (Whom did you see?)",
            "Tumne is sujhav ke liye kise phone kiya tha? (Whom had you called for this suggestion?)", "Aap kise invite karna chahoge? (Whom would you like to invite?)",
            "Vo kitni baar mujhse milega? (How many times will he meet me?)", "Hum kab kab school jayenge? (How often will we go to school?)",
            "Main jaanta hu, aap kahaan rahte ho. (I know where you live.)", "Wahaan kya kya hua? (What all happened there?)", "Tum mujhe sone dete ho. (You let me sleep.)", "Bachche mujhe padhne nahi dete. (Children don’t let me study.)", "Bachche padhne nahi dete. (Children don’t let study.)",
            "Use jaane kyo nahi diya tumne?", "Main use mere ghar aane doonga. (I will let him come my home.)", "Main padhne nahi doonga. (I will not let study.)",
            "Main tumhe padhne nahi doonga. (I will not let you study.)", "Main kisi ko bhi padhne nahi doonga. (I will not let anyone study.)",
            "Main har kisi ko padhne doonga. (I will let everyone study.)", "Usne mujhe kuch nahi karne diya. (He didn’t let me do anything.)",
            "Tumne mujhe khaane nahi diya. (You didn’t let me eat.)", "Sarkaar ne hame building nahi banane di. (The government didn’t let us construct a building.)",
            "Maa ne bachche ko pitne nahi diya. (Mom didn’t let the child beaten.)", "Mummy mujhe TV dekhne deti hai. (Mom lets me watch the TV.)",
            "Mummy mujhe TV nahi dekhne deti. (Mom doesn’t let me watch the TV.)", "kya tum mujhe jaane doge agar main tumhe ₹10 doon to? Will you let me go if I give you Rs 10?",
            "Papa sochne nahi dete or phir daantte hai (Dad doesn’t let think and then scolds.)", "Main tumhe gane sunne doonga par pahle paise do. (I will let you listen to songs but first, you give me money.)", "Vo hame ghar mein nahi ghusne dega. (He’ll not let us enter the house.)",
            "Papa hame ped par nahi chadhne dete. (Dad doesn’t let us climb upon the tree.)", "Bachche mummy papa ko sone nahi denge. (Children will not let mom and dad sleep.)",
            "Main tumhein pen se likhne nahi de sakta. 9I can’t let you write with a pen.)", "Main tumhe aam todne doonga. (I’ll let you pluck the mangoes.)",
            "Us ladke ne mujhe vaha khelne nahi diya. (That boy didn’t let me play there.)", "Is aadmi ne ram ko yaha baithne nahi diya.", "This man didn’t let Ram sit here.",
            "Main tumhe ye nahi karne doonga. (I will not let you do this.)", "Hum kabhi-kabhi use khelne dete the. (We used to let him play sometimes.)",
            "Papa mujhe school jane nahi dete.(Dad doesn’t let me go to school.)", "Mera akelapan mujhe jeene nahi dega. (My loneliness won’t let me live.)",
            "Main tumhare dukh ko badhne nahi doonga. (I’ll not let your pain grow.)", "Usne mujhe car nahi chalane di. (He didn’t let me drive the car.)",
            "Usne mujhe bike nahi chalane di. (He didn’t let me ride the bike.)", "Usne mujhe mobile nahi khareedne diya. (He didn’t let me purchase the mobile.)",
            "Usne mujhe computer nahi chalane diya. 9He didn’t let me operate the computer.)", "Usne mujhe pani nahi peene diya. (He didn’t let me drink water.)",
            "Usne mujhe khana nahi khane diya. (He didn’t let me eat the food.)", "Kya tumne use baithne diya? (Did you let him sit?)",
            "kya usne mujhe phone karne diya?   (Did he let me call/phone/ring?)", "Tumne use jane kyo diya? (Why did you let him go?)", "Tumne use khelne kyo diya? (Why did you let him play?)",
            "Uski mom ne use ghar se niklne nahi diya. (His mother didn’t let him go out of home.)", "Kya tum mujhe chein se jine de sakte ho? (Can you let me live peacefully?)",
            "Tum mujhe shayad na jane do. (You might not let me go.)", "Vo pakka mujhe jane dega. (He’ll definitely let me go.)",
            "Bhagvaan aaj barish hone denge. (God will let it rain today.)", "koi mujhe padhne nahi deta. (Nobody lets me study.)",
            "Main tumhein bazaar jane de raha hoon. (I am letting you go to market.)", "Hum sab use jeene nahi dete. (We all don’t let him live.)",
            "Mujhe Burger kyo nahi khane dete tum? (Why don’t you let me eat Burger?)", "Vo mera dost hai isliye main use galat nahi karne deta. (He is my friend hence I don’t let him do wrong.)",
            "Papa mujhe 18 saal se pehle car nahi chalane denge. (Dad will not let me drive the car before 18.)", "Tum hamein jane do. (You let us go.)",
            "Why do I let you go? (Usne mujhe kabhi rone nahi diya.)", "Usne mujhe kabhi rone nahi diya. (He never let me cry.)", "Tumne mujhe kabhi hansne nahi diya. (You never let me laugh.)",
            "Tum na sote ho na mujhe sone dete ho. (Neither you sleep nor let me sleep.)", "Ya to mujhe jane do ya phir tum jao. (Either let me go or you go yourself.)",
            "Maine us din use phone nahi karne diya. (I didn’t let him call/phone that day.)", "Maine bhi aane nahi diya. (Even I didn’t let come.)",
            "Tumne mujhe sochne tak nahi diya. (You didn’t even let me think.)", "Vo mujhe bhi sone nahi deta. (He doesn’t let me sleep either.)",
            "Main na tumhein khelne doonga na TV dekhne doonga. (I’ll neither let you play nor watch TV.)", "Hum tumhen ek second bhi sochne nahi denge. (We’ll not let you think even for a second.)",
            "Main tumhein mithai khane doonga par tab jab tum mujhe bhi do. (I’ll let you eat sweets provided you give me too.)", "Madam ne class mein sirph mujhe baithne diya. (Madam let only me sit in the class.)",
            "Vo ram ko jeene nahi deta aur tum use marne nahi dete. (He doesn’t let Ram live & you don’t let him die.)", "Main tumhein exercise nahi karne doonga. (I’ll not let you do the exercise.)",
            "Mummy aur papa hamein khelne denge. (Mom and dad will let us play.)", "Kya aap mere bhai ko jane doge? (Will you let my brother go?)",
            "Rahul ne mujhe koi bhi kaam kabhi akele nahi karne diya. (Rahul never let me do any work alone.)", "Vo hum bachchon ko mobile nahi chedne dete hai. (He doesn’t let we kids touch his mobile.)",
            "Boss mujhe sochne tak nahi dete. (Boss doesn’t even let me think.)", "Main sochne kyo doon? (Why do I let think?)", "Ram use kyo jane de? 9Why does Ram let him go?)",
            "Mummy mujhe padhne jaroor degi. (Mom will let me study for sure.)", "Vo mujhe padhne deta hai. (He lets me study.)", "Yaha log mujhe chain se jeene nahi dete. (People here don’t let me live peacefully.)",
            "Bachche padhne nahi dete. (Children don’t let study.)", "kya usne tumhein aane diya? (Did he let you come?)", "Hamein kyo nahi bolne diya tumne? (Why did you not let us speak?)",
            "Sarkaar hame apni baat nahi kahne deti. (The government doesn’t let us speak our perspective.)", "Main tumhein us bachche ko nahi peetne doonga. (I will not let you beat that child.)",
            "Kya tum mujhe milne doge? (Will you let me meet?)", "log use mujhse milne nahi dete. (People don’t let him meet me.)",
            "5 Din lagenge. (It will take 5 days.)", "6 saal lage. (It took 6 years.)", "Aaj somvaar nahi hai. (It is not Monday today.)",
            "Kal chhuttee thee. (It was holiday yesterday.)", "Barish kab hogee? (When will it rain?)", "Kya barish hogee? (Will it rain?)",
            "Kya barish ho rahee hogee? (Will it be raining?)", "Aaj barish hogee (It will rain today.)", "Kal barish hui thee. (It had rained yesterday.)",
            "Kal ole pade the. (It had hailed yesterday.)", "Aaj ole padenge. (It will hail today.)", "Parson ole padenge. 9It will hail day after tomorrow.)",
            "Parson ole pade the. (It had hailed day before yesterday.)", "Parson chhuttee thee. 9It was holiday day before yesterday.)", "Parson chhuttee hogee. (It will be holiday day after tomorrow.)",
            "2 minute lagte hai. (It takes 2 minutes.)", "2 Minute lag rahe hai. (It is taking 2 minutes.)", "2 Minute lage hai. (It has taken 2 minutes.)",
            "2 Minute lage. (It took 2 minutes.)", "2 Minute lag rahe the. (It was taking 2 minutes.)", "2 Minute lage the. (It had taken 2 minutes.)",
            "2 Minute lagenge. (It will take 2 minutes.)", "Mujhe 2 Minute lagenge. (It will take me 2 minutes.)", "Ab Tumharee baree hai. (It is your turn now.)",
            "Yah tumhara pyar hai. (This is your love.)", "Yah kya hai? (What is this?)", "2 din ho gaye hai. (It has been 2 days.)",
            "2 Din ho gaye the. (It had been 2 days.)", "Mujhe 2 din ho gaye the. (It had been 2 days to me.)", "Kya kal chhuttee thee? (Was it holiday yesterday?)",
            "Use kitna samay lagega? (How much time will he take?)", "Aaj ole padane the.", "It had to hail today. (It may hail today.)",
            "Kya Dehradun mein barish huai? (Did it rain in Dehradun?)", "Barish kyo hotee hai? (Why does it rain?)", "Yah pyar hai. (It is love.)",
            "Yah ek kursee hai. (It/This is a chair.)", "Tum kitna samay loge? (How much time will you take?)", "Main das minat loonga. 9I will take 10 minutes.)",
            "Us din kya tha? (What was it on that day?)", "Andhera ho raha hai. (It is getting dark.)", "Umas ho rahee hai. (It is getting humid.)",
            "Yah meree khvahish hai. (It’s my will.)", "Waha bahut barish hoti h. (It rains a lot there.)", "Aaj barish honi hai. (It has to rain today.)",
            "Aaj barish honee thee. (It had to rain today.)", "Ye tumhari soch hai. (It is your thinking.)", "Ye kiska pen hai? (Whose pen is this/it?)",
            "Iskee keemat 5 Rupya hogee. (It will be for Rs 5.)", "Kya aaj somvaar hai? (Is it Monday today?)", "Yaha barish ho rahee hai. (It is raining here.)",
            "Vaha ole pad rahe the. (It was hailing there.)", "Kya kal barish hogee? (Will it rain tomorrow?)", "Mujhe do ghante lage. (It took me 2 hrs.)",
            "Tumse mile hue mujhe 2 din ho gae the. (It had been 2 days to me having met you.)", "Tumhen itna samay kyon lag raha hai? (Why are you taking this much time?)",
            "Mujhe ghar pahuchne me 5 hour lagte h. (It takes me 5 hours to reach home.)", "Mujhe school pahuchne mein aadha ghanta lagta hai. (It takes me half an hour to reach school.)",
            "Hamen computer seekhne mein 3 maheene lage. (We took 3 months to learn computer.)", "Tumhe dhoodne me mujhe 5 min lage. (I took me 5 minutes to find/search you.)",
            "Ye kaam karne me kaafi samay lagega. (It will take plenty of time to do this work.)", "Aaj Tumhara din hai, kal mera hoga.(It’s your day today, it’ll be mine tomorrow.)",
            "Tumhe dekhe hue mujhe 10 din ho gaye. (It has been 10 days to me having seen you.)", "Office pahunchne mein kitna samay laga? (How much time did it take to reach office?)",
            "Office pahunchne mein tumhen kitna samay laga? (How much time did you take to reach office?)", "Kitaab khatam karne mein mujhe. 2 maheene lage. (It took me 2 months to finish the book.)",
            "Seeta ko kaaphee saal lage. (Seeta took lots of time.)", "IAS adhikaree banne mein 3 saal lage. (I took 3 years to become an IAS officer.)",
            "Ye kapda silne mein 20 din lagenge. (It will take 20 days to stitch this cloth.)", "Agar tum pencil se likho, to kafi samay lagega. (If you write with a pencil, it will take too long.)",
            "Ye khatm karne mein tum kitna samay loge? (How much time will you take to finish it?)", "Tumhe dekhe hue mujhe kareeb 2 saal ho gaye hai. (It’s been about 2 years to me having seen you.)",
            "Jab main vaha pahuncha, barish ho rahee thee. (When I reached there, it was raining.)", "Khana khaye hue mujhe kai din ho gaye hai. (It has been many days to me having had the food.)",
            "Achchhe kapde pahne hue kai din ho gaye hai. (It’s been many days having worn good clothes.)", "Vaha gae hue mujhe kai din ho gaye hai. (It’s been many days to me having gone there.)",
            "Khana khaye hue mujhe kai din ho gaye. (It has been many days to me having had the food.)", "Aaj ole pad sakte the par nahi pade. (It may have hailed today but didn’t.)",
            "Vo meree jindgee ka bahut aham din tha. (It was a very important day of my life.)", "Burger khaye hue mujhe 4 maheene ho gaye hai. (It’s been 4 months to me having eaten/had Burger.)",
            "Tumhen dekhe hue mujhe 3 din ho gaye hai. (It has been 3 days to me having seen you.)", "Use ghar pahunchne me kitna samay laga? (How much time did he take to reach home?)",
            "Match ka anand lete 2 ghante ho gae hai. (It has been 2 hrs enjoying the match.)", "Us din poore desh mein chhuttee thee. (It was holiday that day in the whole country.)",
            "Subah se barish ho rahee thee. (It had been raining since morning.)", "Tumhe dekhe hue mujhe kareeb 2 saal ho gaye hai. (It’s been about 2 years to me having seen you.)",
            "Jab main vaha pahuncha, barish ho rahee thee. (When I reached there, it was raining.)", "Khana khaye hue mujhe kai din ho gaye hai. 9It has been many days to me having had the food.)",
            "Achchhe kapde pahne hue kai din ho gaye hai. (It’s been many days having worn good clothes.)", "Vaha gae hue mujhe kai din ho gaye hai. (It’s been many days to me having gone there.)",
            "Khana khaye hue mujhe kai din ho gaye. (It has been many days to me having had the food.)", "Aaj ole pad sakte the par nahi pade. (It may have hailed today but didn’t.)",
            "Vo meree jindgee ka bahut aham din tha.  (It was a very important day of my life.)", "Tum mere danyee aur ho. (You are right to me.)",
            "Main uske aage khada tha. (I was standing ahead of him.)", "Bachcho ke thik samne teacher khade hai. (The teacher is standing just in front of the students.)",
            "Tumhare bayee or kaun hai? (Who is to the left of you?)", "Mere bagal me dayee or Seeta baithi hai. (Seeta is sitting immediate right to me.)",
            "Main tumhare peeche tha. (I was behind you.)", "Uske peeche kitne log khade hai? (How many people are standing behind him?)", "Mere bayee or koi ladka nahi tha. (There was no boy left to me.)",
            "Mere ghar ke saamne tumhara ghar hai. (Your house is in front of my house / mine.)", "Mera ghar tumhare ghar se theek aage vala hai. (My house is just ahead of yours.)",
            "Mera ghar tumhare ghar se theek peeche vala hai. (My house is just before yours.)", "Mere dayee or koi nahi hai. (There is no one to the right of me.)",
            "Kya tumhare dayee or koi hai? (Is there someone to the right of you?)", "Kya tumhare bayee or koi nahi hai? (Is there no one to the left of you?)",
            "Kya tumhare aage koi hai? (Is there someone ahead of you?)", "Kya tumhare peeche koi nahi hai? (Is there no one behind you?)", "Kya tumhare samne koi hai? (Is there someone in front of you?)",
            "Kya tumhare bagal mein koi hai? (Is there someone next to you?)", "Hum bayee or khade the. (We were standing on the left.)", "Hum dayee or khade the. (We were standing on the right.)",
            "Uske samne kaun tha? (Who was there in front of him/her?)", "Mere aage line mein teen log khade the. (There were three people standing ahead of me in the queue.)",
            "Mere peeche line mein kitne the? (How many were there behind me in the queue?)", "Mere joote kamre ke ek kone mein pade the.(My shoes were lying in a corner of the room.)",
            "Tumhare joote ke samne vale joote mere hai. (The shoes beside yours are mine.)", "Vo tumhare kis taraf hai? (Which side is he to you?)",
            "Vo mere dayee or hai. (He is to the right of me.)", "Dayee or to theek hai par vo kya bagal mein hai?(Right side is okay; but is he adjacent?)",
            "Hamare beech 2 ladkiyan hai. (There are 2 girls between us.)", "Yah tasveer kiksi hai? (Whose is this painting?)", "Shor mat karo. (Don’t make a noise.)",
            "Kya aap aam kha chuki hain? (Have you eaten the mango?)", "Aap Kahan Jaoge? (Where will you go?)", "Unke pass apne baig hai. (They have their bags.)",
            "Kya yaha paas me dawa ki koi dukaan hai? (Is there a chemist nearby here?)", "Main Kahta hu, ruko. (I say, stop.)",
            "Aaj chhutti ka din hai. (It is holiday today.)", "Yah bahut dur hai. (It’s quite far.)", "Iska matlab maine galti ki. (That means I made a mistake.)",
            "Main car nahin chalaunga. (I will not drive the car.)", "Vah pratidin school jati hai. (She goes to school everyday.)", "Main market ja raha hu. Sath chaloge? (I’m going to Market. Will you come along?)",
            "Nahi. Abhi main Vyast hu. (No. I am busy at the moment.)", "Ladke subah se nadi mein tair rahe hain. (Boys have been swimming in the river since morning.)",
            "Vah angreji seekh raha hai. (He is learning English.)", "Mere Pass Meri Car hai. (I have my car.)", "Tumhare pass tumhara pen hai. (You have your pen.)",
            "Tum kis kaksha mein padhate ho? (In which class do you study?)", "Mera bhai bhopal mein rahata hai. (My brother lives in Bhopal.)",
            "Tumhare pass meri book hai. (You have my book.)", "Mata ji khana bana rahi thi. (Mom was cooking food.)", "Aapki umar kitani hai? (What is your age? / How old are you?)",
            "Kal me ek bas me yatra kar raha tha. (Yesterday I was travelling in a bus.)", "Kya tumne aisi cheejo ka anubhav kiya? (Have you experienced such things?)",
            "Flight ka kiraya Train ke kiraye ke barabar hoga. (Flight fare will be equal to train fare.)", "Iska Chehra balon se kyu Dhaka huwa hai? (Why is its face covered with hair?)",
            "Main chay pasand karata hun jabaki vah coffee pasand karati hai. (I prefer tea, whereas/while she prefers coffee.)", "Kolkata Mumbai ki tulna me zyada bada hai (Kolkata is quite bigger than Mumbai.)",
            "Main tumhe tasveer ke bare me bata raha tha. (I was telling you about the painting.)", "Achanak use ek chutkula yaad aya. (Suddenly, he remembered a joke.)",
            "Could I meet you tomorrow? (Could I meet you tomorrow?)", "Vah tumhari sahayta kar sakta tha. (He could have helped you.)", "Vah cricket khel raha hai.(He is playing cricket.)",
            "Vah kal indore ja rahi hai. (She is going to Indore tomorrow.)", "Vah apne pitaji ki madad kar raha hai. (He is helping his father.)",
            "aaj kon sa din hai? (What is the day today?)", "mere saath majaak kar rahe ho kya (Are you not kidding me)", "bahane mat banao. (Don’t make excuses.)",
            "mujhe naraj mat kro (Don’t annoy me.)", "mujhe gussa mat dilao. (Don’t make me angry.)", "gussa mat ho. (Don’t be annoyed/ angry.)",
            "mujhe pareshan mat karo. (Don’t bother me.)", "mujhase bahas mat karo. (Don’t argue with me.)", "apne haath achchhe se saaf kar lo. (Wash your hands properly.)",
            "apna chehra achchhe se saaf kar lo. (Wash your face properly.)", "kitab ko achchhe se pakdo. (Hold the book properly.)", "chupachaap baithe raho, hilo mat. (Sit still. Don’t move.)",
            "kya main nahin ja sakata? (Can I not go?", "main kahan ja sakata hun? (Where can I go?)", "main kahan-kahan ja sakata hun? (Where all can I go?",
            "aap kisake saath baith sakate ho? (With whom can you sit?)", "Wo America nahi ja paya. (He couldn’t go to America.)", "Hamari teem kahan khel rahi hogi? (Where will our team be playing?)",
            "Vah agle ravivar Shalini se vivaah karega. (He will be marrying Shalini next Sunday.)", "Zarurat me kaam ayaa dost hi sachha dost hai. (A friend in need is a friend indeed.)",
            "Apne bachho ko apna kam karne do. (Let your children do their work themselves.)", "Robin ne ek tasveer banayi. (Robin drew a picture.)",
            "Ayiye tumhara janam divas manaye. (Let’s celebrate your birthday.)", "Hamne Apne bachchon ko bhagwan me viswas dilaya hai. (We have made our children believe in God.)",
            "Maine ye pustaken use nahin di. (I didn’t give him these books.)", "Usne mere patr ka uttar nahin diya. (He did not reply to my letter.)",
            "Unhone taro ko bhi kat diya tha. (They had even cut off all the wires.)", "6 Ghant e ke bad bad ve chale gaye. (They left after six hours.)",
            "Kya usne nili shart pahni? (Did he wear a blue shirt?)", "Jail me kaidiyo se janwaro se badtar saluk kiya jaata hai. (In prison, captives are treated worst than animals.)",
            "Kal main ek bas me yatra kar raha tha. (Yesterday I was travelling in a bus.)", "Abhi tak koi nahin aaya hai. (Nobody has come yet.)",
            "Uska beta sena mein bharti ho gaya hai? (His son has been selected in army?)", "Ham log unke prati hamdardi dikhana chahte hai. (We people want to show pity on them.)",
            "Uske jaane ke baad hamne khana khaya. (We had the food after he left.)", "Machhuaron ne machhali pakad lee thi. (Fishermen had caught fishes.)",
            "Usne kaha ki vah apna kaam kar raha tha. (She said that she had been doing his work.)", "Robin ne dhan udhaar liya. (Robin borrowed the money.)",
            "Tum subah se yahan kyon baithe huye ho? (Why have you been sitting here since morning?)", "Agar tumhe meri madad ki zarurat ho to kripya mujhe batao. (If you need my help, please let me know.)",
            "Log aksar mujhe poochhte hai. (People often ask me.)", "Aap kab-kab khelne jaate ho? (How often do you go to play?)", "Rahul kaha- kaha jata hai? (Where all does Rahul go?)",
            "Riya apna kaam khud karti hai. (Riya does her work herself.)", "Mai ghumne jata hu. (I go for a walk)", "Aap kya karte ho? (What do you do?)",
            "Ye sunne mein accha lagta hai. (It sounds good.)", "Aap kis kis ke saath khelte ho? (Who all do you play with?)", "Mai cricket khelta hu. (I play cricket.)",
            "Mai cricket nahi khelta hu. (I don’t play cricket.)", "Kya mai cricket khelta hu? (Do I play cricket?)",
            "Kya mai cricket nahi khelta hu? (Do I not play cricket?)", "Wo aksar ghoomta hai. (He often walks.)", "Wo shayad hi kabhi ghoomta hai. (He seldom walks.)",
            "Aap kya-kya khate ho? (What all do you eat?)", "Aap kab-kab khelne aate ho? (How often do you come to play?)", "Wo kise pyar karta hai? (Whom does he love?)",
            "Sanjeev kaha kaha jata hai? (Where all does Sanjeev go?)", "Mai use ghar bhej deta hu. (I send him home.)", "Wo kiski car chalati hai? (Whose car does she drive?)",
            "Aap kis-kis ke saath bat karte ho? (Who all do you speak with?)", "Kaun jata hai? (Who goes?)", "kaun- kaun jate hai? (Who all go?)",
            "Maine chaku se cake kata. (I cut the cake with a knife.)", "Usne neele pen se patr likha. (He wrote the letter with a blue pen.)",
            "Hum aapke saath rahenge. (We will stay with you.)", "Mere papa apne office laptop le ke gaye. (My dad brought laptop to his office.)",
            "Tum imandari ke saath jeete ho. (You live with honesty.)", "Main 2 baje paida hua tha. (I was born at 2 o’clock.)", "Main ek hotel mein ruka. (I stayed in a hotel.)",
            "Wo bus stop par khada tha. (He was standing at the bus stop.)", "Main dopahar mein wahan tha. (I was there in the afternoon.)",
            "Hum raat mein padhte hain. (We study at night.)", "Yeh fruit 10 rs kilo bik raha hai. (This fruit is selling at Rs. 10 a kg.)",
            "Main pyaaj 10 rs kilo khareed raha hun. (I am buying onion at Rs. 10 a kg.)", "Main Holi par ghar aunga. (I will come home at Holi.)",
            "Main Diwali par ghar aunga. (I will come home at Diwali.)", "Wo uske janmdin par wahan jayega. (He will go there at his birthday.)",
            "Ayansh ab 10 baje sota hai. (Ayansh sleeps at 10 now.)", "Wah raat mein hospital gaya. (He went to hospital at night.)", "Papa deepawali mein wahan jaenge. (Papa will go there at Diwali.)",
            "Ham janamdin par aaye the. (We had come at birthday.)", "Main glass se dekh sakta hun. (I can see through the glass.)", "Mai is ched ke zariye andr dekh skta hu. (I can see inside through this hole.)",
            "Main ek gali se gujar raha tha. (I was passing through a street.)", "Ham bhumigat raste se gaye. (We went through the underpass.)",
            "Paani is pipe se gujar raha hai. (Water is passing through this pipe.)", "Main khidki se tumhe dekh sakti hun. (I can see you through the window.)",
            "Usne mujhe is chhed se dekha. (He saw me through this hole.)", "Hum surang se gaye. (We went through an underpass.)", "Aman khidki se jhank raha hai. (Aman is peeping through the window.)",
            "Paani is pipe se gujra. (Water passed through this pipe.)", "Water passed through this pipe. (There is a temple beyond this mountain.)",
            "Us nadi ke paar kya hai? (What is there beyond that river?)", "Is Brahmand ke paar kuchh hai. (There is something beyond this universe.)",
            "Soch se pare bhi chije hai. (There are things beyond imagination.)", "Iske paar ek jungle tha. (There was a forest beyond it.)",
            "Yeh train kanpur se hote hue Delhi jayegi. (This train will go to Delhi via Kanpur.)", "Maine use apni photo bluetooth se bheji. (I sent him my picture via Bluetooth.)",
            "Bas kotdwara se hote hue aati hai. (Bus comes via Kotdwara.)", "Main Dubai ke raaste America gaya. (I went to America via Dubai.)",
            "Use bluetooth se bhej do. (Send him via Bluetooth.)", "Mera ghar tumhari dukan ke thik samne h. (My house is just opposite to your shop.)",
            "Wo tumhare samne khada tha. (He was standing opposite you.)", "Jo tum kehte ho Ram uska ulta karta hai. (Ram does opposite of what you say.)",
            "Uski soch tumhari soch se thik ulti hai. (His thinking is opposite yours.)", "Wo aapke kahe ka ulta karta hai. (He does opposite of what you say.)",
            "Uski soch meri soch ke vipreet hai. (His thinking is opposite mine.)", "Tum mere thik samne baithoge. (You will sit opposite to me.)",
            "Wo pakka iska ulta hi karega. (He must do its opposite.)", "Mera naam list me tumhre naam ke upar h (My name is above your name is the list.)",
            "Paisa pyar se upar hai. (Money is above love.)", "kewal uski ankhe pani ke upar thi. (Only his eyes were above water.)", "Mera desh sabse pehle hai. (My country is above all.)",
            "Bhukamp ki tivrata Richter scale mein 8 se upar hai. (The magnitude of the earthquake is above 8 in Richter scale.)",
            "Mummy ji ped ke niche baithi hai. (Mom is sitting under the tree.)", "Wo pul ke niche tha. (He was under the bridge.)",
            "Mera ghar aapke ghar ke niche hai. (My house is underneath your house.)", "Uski tasvir meri kitab ke niche hai. (Her photograph is beneath my book.)",
            "Patr meri kitab ke niche hai. (The letter is underneath the book.)", "Mobile takiye ke niche hai. (The mobile is beneath/underneath the pillow.)",
            "Mera name list mein tumhare naam ke niche hai. (My name is below your name in the list.)", "Uska muhh pani ke niche tha par naak upar. (His mouth was below water but nose was above.)",
            "Mere dasvi mein 70 se niche hai. (I scored below 70% in 10th.)", "Bhukamp ki tivrata Richter scale mein 9 se niche hai. (The magnitude of the earthquake is below 9 in Richter scale.)",
            "Mera nam Ram ke nam ke niche hai. (My name is below Ram’s.)", "Uska star tumse niche hai. (His level is below yours.)", "Kimate niche ja rahi hai. (Prices are going down.)",
            "Paani ka star niche ja raha hai. (Water level is going down.)", "Gubbara niche ja raha hai. (Balloon is going down.)", "Wo Yash aur Vaibhav ke beech mein baithi hai.(She is sitting between Yash and Vaibhav.)",
            "Pen do computeron ke beech mein pada hua hai. (The pen is lying between two computers.)", "Uski tasvir teen aur chijo ke beech rakhi hui thi. (Her photograph was kept among 3 other items.)",
            "Main ghar se bahar nikal raha hun. (I am getting out of my home.)", "Wah Delhi se bahar ja chuka hai. (He has gone out of Delhi.)",
            "Rahul ne 100 me se 90 no. praapt kiye. (Rahul scored 90 out of 100.)", "15 mein se keval 2 hi vidharthi hain. (There are only 2 students out of 15.)",
            "Yahan se bahar niklo. (Get out of here!)", "Wahan 8 mein se keval 1 hi vidharthi hai (There is only 1 student out of 8.)", "10 mein se 3 ke pas mobile the. (3 out of 10 had mobiles.)",
            "Pen table par rakha hua hai. (The pen is kept on the table.)", "Rohit hathi ke upar baitha hua hai. (Rohit is sitting on the elephant.)",
            "Main Sunday ko gaya. (I went on Sunday.)", "Vah 20 December 2012 ko aaya. (He came on 20th December 2012.)", "Kitab laptop par rakhi hai. (The book is kept on the laptop.)",
            "Hamlog somvaar ko aayenge. (We all will come on Monday.)", "Main kood kar ghode par baith gaya. (I jumped upon the horse.)", "Jhadu apne aap table par aa gaya. (The broom came upon the table by itself.)",
            "Main ghode ke upar kudaa.(I jumped upon the horse.)", "Ram tumhare viruddh kyon hain? (Why is Ram against you?)", "Wo mere khilaaf use bhadka raha hai. (He is provoking him against me.)",
            "Kya tum mere virodhi ho? (Are you against me?)", "Aman mere viruddh nahi ho sakta. (Aman can’t be against me.)", "Wo mere bare mein baat kar raha hai. (He is talking about me.)",
            "Papa mere bare mein jaante hai. (Papa knows about me.)", "Uske bare mein baat mat karo. (Don’t talk about him.)",
            "Wo teen bhaiyo ki behan hai. (She is the sister of 3 brothers.)", "Yeh kursi us dukan ki hai. (This chair is of that shop.)", "Yashi hamare ghar aa rahi hai. (This chair belongs to that shop.)",
            "This chair belongs to that shop. (Yashi is coming to our home.)", "Wo hospital ja raha hai. (He is going to hospital.)", "Main school ki taraf ja raha hun. (I am going towards school.)",
            "Wo tumhari taraf aa raha tha. (He was coming towards you.)", "Yashi hamare ghar ki taraf aa rahi hai. (Yashi is coming towards our home.)",
            "Wo hospital ki or ja raha hai. (He is going towards hospital.)", "Main rassi ke upar se kuda. (I jumped over the rope.)", "Hamare sir ke upar chhat hai. (Hamare sir ke upar chhat hai.)",
            "Nadi ke upar ek pul hai. (There is a bridge over the river.)", "Ayansh rassi ke upar se kuda. (Ayansh jumped over the rope.)", "Uska bhashan mere sir ke upar se gaya. (His lecture passed over my head.)",
            "Main 5 minute mein aa jaunga.(I will come within 5 minutes.)", "Wo 2 din mein hi wapas aa gaya hai. (He has come back within just 2 days.)",
            "Wo 2 din ke andar aa raha hai. (He is coming within 2 days.)", "Main tumse behtar hun. (I am better than you.)", "Hum usse lambe hai.(We are taller than him.)",
            "Humne raat bhar kam kiya. (We worked throughout the night.)", "Humne sari raat kam kiya. (We worked throughout the night.)", "Main tumhare bina kuchh nahi hun. (I am nothing without you.)",
            "Uske bina ham akele hai. (We are alone without him.)", "Keematein badh rahi hain. (Prices are rising up.)", "Gubbara upar ja raha hai. (Balloon is going up.)",
            "Pani ka star badh raha hai. (Water level is going up (rising).", "Usne pathar upar ki taraf phenka. (He threw the stone upwards.)", "Main upar ki or jaa raha hun. (I am going upwards.)",
            "Pathar ko upar ki or phenko. (Throw the stone upwards.)", "Gubbara upar ki or gaya. (Balloon went upwards.)", "Usne pathar niche ki taraf phenka.(He threw the stone downwards.)",
            "Usne niche ki or dekha. (He looked downwards.)", "Wo do saal pehle aaya. (He came 2 years ago.)", "Papa char din pehle aaye the. (Papa had come 4 days ago.)",
            "Wo 36 sal pahle award jita. (He won the award 36 years ago.)", "Computer ke andar kya hai? (What is there inside the computer?)",
            "Usne ghar ke andar dekha. (He looked inside the house.)", "Main ghar ke andar tha. (I was inside the house.)", "Wo ghar ke bahar khada tha. (He was standing outside the house.)",
            "Wo school parisar ke bahar tha. (He was outside the school premises.)", "Wo school parisar ke bahar hai. (He is outside the hospital premises.)",
            "Vah mere bagal mein khada tha. (He was standing next to me.)", "Wo ram ke bagal mein baitha hai. (He is sitting next to Ram.)", "Papa ayansh ke bagal mein baithe hai. (Papa is sitting next to Ayansh.)",
            "Rashmi Divya ke bagal mein baithi hai. (Rashmi is sitting adjacent Divya.)", "Masood Tarun ke bagal mein khada hai. (Masood is standing byTarun.)",
            "Ram Mayank ke bagal mein baitha hai.(Ram is sitting beside Mayank.)", "Main line mein Ram ke piche tha. (I was behind Ram in the queue.)",
            "Uske piche koi nahi khada hai. (There is nobody standing behind him.)", "Avirat Ayansh ke piche pada hai. (Avirat is sitting behind Ayansh.)",
            "Ram Mayank ke piche baitha hai. (Ram is sitting behind Mayank.)", "Main line mein Ram ke aage tha. (I was ahead of Ram in the queue.)",
            "Uske aage koi nahi khada hai. (There is no one standing ahead of him.)", "Mera din achchha guzar raha h. (I’m having a good day.)",
            "Mera time achchha kat raha hai. (I’m having a good time.)", "Car hona meri dili ichchha hai. (To have a car is my whole-hearted will.)",
            "Wo nikal gaya hoga. (He would have left.)", "Aapne use call kiya hoga. (You would have called him.)", "Aap rakh lijiye ye pen. (You keep this pen.)",
            "Mere pair mein dard hai. (I have a pain in my leg.)", "Mujhe jana pad raha hai. (I am having to go.)", "Rahul ko sochna pad raha hai. (Rahul is having to think.)",
            "Hamesha ke liye rakh lijiye (Keep it forever.)", "Usne khana kha liya tha. (He had eaten the food.)", "Avirat burger kha raha hai. (Avirat is eating burger.)",
            "Apke paas kitna paisa hai? (How much money do you have?)", "Lgta hai uske paas pen h. (Seems as, he has a pen.)", "Hame ghar jana hoga. (We will have to go home.)",
            "Hame samjhauta karna hoga. (We will have to compromise.)", "Mujhe khelna hai. (I have to play.)", "Hame nahi khelna hai. (We have not to play.)",
            "Kya mujhe jana hai? (Have I to go?)", "Tumhare paas kya hai? (What do you have?)", "Maine khana kha liya hai. (I have eaten the food.)",
            "Aap ye apne pas rakhiye. (You keep it with yourself.)", "Ye lijiye. (Have it please.)", "Aapke pas kya tha? (What did you have?)",
            "Wo paani pee raha hai. (He is drinking water.)", "Mere paas car honi chahiye thi. (I should have had a car.)", "Uske pas kuch hona chahiye tha. (He should have had something.)",
            "2019 tak mere paas car ho sakti hai. (I may have a car by 2022.)", "Iske bawajood bhi, mai tumhe chahta hu. (Having said that, I love you.)",
            "Aapse milne ke bad mai nikal gaya. (Having met you, I left.)", "Tumhe dekhne ke baad mai tumhare sapno mein kho gaya. (Having seen you, I got lost in your dreams.)",
            "Aisa ghar mere pas ho, mera sapna tha. (To have such a house was my dream.)", "Aisa pati milna meri  khwahish thi. (To have such a husband was my will.)",
            "Aap jaisa adhyapak pakar main khush hun.(I am happy to have such a teacher like you.)", "Uske paas car ho sakti hai. (He may have a car.)",
            "Tumhe pana meri zindagi ki dili khwahish rahegi. (To have you with me will remain the whole-hearted desire of my life.)",
            "Rahul ke paas shayad paise na ho. (Rahul may not have money.)", "Lagta hai tumhare paas dimag nahi hai. (It seems that you don’t have brain.)",
            "Aapse mile hue mujhe do saal ho gaye hai. (It has been 2 years to me having met you.)", "Rahul ko dekhe hue kai din ho chuke hai. (It has been many days having seen Rahul.)",
            "Sath khele hue  kafi samay ho gaya. (It has been a long having played together.)", "Paisa hona achi baat hai par ghamand nahi hona chahiye. (It is good to have money but there should not be arrogance.)",
            "Ho sakta hai uske paas kitab ho. (He may have a book.)", "Ho sakta hai rahul ke paas mobile ho. (Rahul may/might/could have a mobile.)",
            "Aasman mein kitne taare hai? (How many stars are there in the sky?)", "Glass me kitna paani tha? (How much water was there in the glass?)",
            "Mujhe fizul main aap se sawal puchne padh rahe hai. (I am having to ask you questions for no reason.)", "Hame bheja Ja Raha hai. (We are being sent.)",
            "Rahul  swarthi ho raha hai. (Rahul is being selfish.)", "Mujhe bataya gaya tha. (I had been told.)", "Wo mere hone wale Pati hai. (He is my to-be husband.)",
            "Wo ghar par hona chahiye (He should be at home.)", "Rohit ko pita ja raha tha. (Rohit was being beaten.)", "Hame Khush Kiya ja raha hai. (We are being made happy.)",
            "Aap thake Huye Lagte hain. (You seem to be tired.)", "Swarthi mat baniye. (Don’t be selfish.)", "Ye Kaam Kiya jana hai (This work is to be done.)",
            "Wo Delhi mein kabhi nahi raha hai. (He has never been in Delhi.)", "Aapko Poocha Jana chahiye tha. (You should have been asked.)", "Use  Mumbai nahi bheja Ja saka. (He could not be sent to Mumbai.)",
            "Wo badal raha hai. (He is being /getting changed.)", "Mai thak raha hun. (I am being / getting tired.)", "Use bheja ja raha Hoga. (He will be getting sent.)",
            "Kamron ko saaf Kiya jana hai. (The rooms are to be cleaned.)", "Kamre ko saaf Kiya ja raha tha. (The room was being cleaned.)",
            "Kaha gaya ki building Banai Jaani hai. (The building was said to be constructed.)", "Kaha gaya ki building Banai ja chuki hai. (The building was said to have been constructed.)",
            "Paise Ke maamle mein swarthi bano. (Be selfish for money matters.)", "Ayansh is waqt school mein nahi hona chahiye. (Ayansh should not be in school right now.)",
            "Mujhe 6:00 baje tak office mein hi Rehna Hai. (I have to be in office itself till 6 o’clock.)", "Use ghar par rehne ke liye Kaha gaya hai. (He has been told to be at home.)",
            "Use Naukri se kyun nikala Gaya? (Why was he fired from the job?)", "Bachcho ko school mein khilaune Diye Gaye. (Kids were given toys in school.)", "Aapse police ke dwara puchtach ki Jani hai. (You have to be interrogated by the police.)",
            "Wo Mere Pitne ke bare mein baat kar rahe the (They were talking about my being beaten.)", "Pakde jaane ke dar se wo dara hua tha. (He was scared of being caught.)", "Kabhi kabhi Swarthi Hona acha Hota Hai. (Being selfish at times is fair.)",
            "Is wakt ghar par hona mere liye zaruri hai. (Being at home is important for me right now.)", "Main apse milne ko Betab ho raha hun. (I am being impatient/crazy to meet you.)",
            "Mujhe team Mein khelne nahi diya ja raha hai. (I am not being let play in the team.)", "Meri Shadi ko 5 saal ho gaye hai. (It has been 5 year to my marriage.)",
            "Dant khane ke baad wo dukhi ho gya. (Having been scolded, he turned/got sad.)", "Ek adyapak hone ke naate ye mera farz hai. (Being a teacher, it is my duty.)",
            "Wo ghar par rehna pasand karta hai. (He loves being at home.)", "Ghar par hone ki wajah se mai apse mil paya. (I could meet you due to being at home.)",
            "Yuddh mein maare jaane ki kya sambhavna hai? (What is the possibility of being killed in the war?)", "Aapke daant khane ki kya Sambhavna hai? (What is the chance of your being scolded?)",
            "Wo Mujhe Peetne ke bare mein baat kar rahe the. (They were talking about beating me.)", "Mujhe Shaam 7:00 baje tak office me rehne ka aadesh kiya gaya tha. (I had been instructed to stay/be in office till 7 PM.)",
            "Hame police ke dwara bahut Buri Tarah Pratadit kiya Gaya. (We were tortured very badly by the police.)", "Aapko jhoot bola gaya tha ki aaj meri Shaadi hai (You had been told a lie that it was my marriage that day.)",
            "Usne mujhe Tumhare bhai ke dwara madad kiye jane ke bare mein Bataya. (He told me about having been helped by your brother.)", "Wo Tumhare Pitne ke bare mein baat kar rahe hai. (They are talking about your being beaten.)",
            "Tumhare sath Hona Meri Zindagi Ki Sabse Badi uplabdhi hai. (Being with you is the biggest achievement of my life.)", " (Aarop Lagaye jane ke bad usne Nirdosh vyavahar karna Shuru kar diya. (Having been accused, he started behaving innocent.)",
            "Lagta hai wo Dehradun mein Kafi Samay se hai. (He seems to have been in Dehradun for quite a long.)", "Aapka school mein Hona Kitna maayne Rakhta hai? (How important is it for you being in school?)",
            "Main nahi gaya.(I didn’t go.)", "Tum kaha jaate ho? (Where do you go?)", "Hum kis tarah aayenge? (How will we come?)", "Main kab tak tumhara saath doonga? (How long will I support you?)",
            "Rohit ki behan kaha ja rahi hai? (Where is Rohit’s sister going?)", "Vo kis shahar se aaya tha? (From which city had he come?)", "Vo ladka aisa kyon sochta hai? (Why does that boy think so?)",
            "Ye bachche kaha rahte hai? (Where do these children live?)", "Vo kal se dance abhyaas kr rahi hai. (She’s been practicing dance since yesterday.)", "Rakesh nahi samajhta. (Rakesh doesn’t understand.)",
            "Ghav se khoon nikal raha hai.(Blood is oozing from the wound.)", "Main padhoonga. (I will study.)", "Vo sabhi kiske bhai hai? (Whose brothers are they all?)", "Vo khidkee se jhaank raha tha. (He was peeping through the window.)",
            "Bachche tab school se aa rahe honge. (Children will be coming from school then.)", "Main shimla aksar jata hoon. (I often go to Shimla.)",
            "Vo khoob padhta hai. (He studies a lot.)", "Vo kabhi-kabhi mere ghar aata hai. (He sometimes comes my home.)", "Kal 6 baje bus nikal chuki hogi. (Bus will have left by 6 o’clock tomorrow.)",
            "Tum kab tak yaha thahroge? (For how long will you stay here?)", "Ma apne bachche ko doodh pila rahi hogi. (The mother will be feeding her child.)",
            "Hum bahut der tak sote hai. (We sleep till late.) ", "Vo ghar se nahi aaya hai. (He has not come from home.)", "Vo apne sapno ko saakaar karega. (He will fulfill his dreams.)",
            "Main kiske bare mein sochta hoon? (Whom do I think about?)", "Main aur tum kiske saath khel rahe the? (With whom were I & you playing?)",
            "Hum vaha gaye the. (We had gone there.)", "Mera dost kaun si car chala raha hai? (Which car is my friend driving?)", "Usne us admi ko jaan se maar diya. (He killed that man.)",
            "Mere saath kai film dekh rahe the. (There were many, watching movie with me.)", "Humne TV mein kiski ladai dekhi? (Whose fight did we watch on TV?)",
            "Hum Ram ko bilkul nahi jante. (We don’t know Ram at all.)", "Ye toota hua dil kuch keh raha hai. (This broken heart is saying something.)",
            "Tum bahut tej daud rahe the. (You were running very fast.)", "Kaun jaayega? (Who will go?)", "Kis hero ka dost tumhare papa ke saath job karta hai? (Which actor’s friend works with your dad?)",
            "Main aksar uske ghar jata hoon. (I often go to his home.)", "Seema aaye din tumhe homework likhne ke liye bulati hai. (Seema often calls you to write homework.)",
            "Bachche subah se TV dekh rahe hai.(Kids have been watching TV since morning.)", "Use kis party ka saath nahi mil raha hai ? (Which party’s support is he not getting?)",
            "Vo mobile se kya dekh raha hai? (What is he watching on Mobile?)", "Main kis ladki ka bhai hoon? (Which girl’s brother am I?)", "Uske papa ne mujhse baat karna pasand kyon nahi kiya?",
            "Why did his father not prefer/like to talk to me? (Ram mushkil se hi kabhi mere ghar aata hai.)", " (Kya ye batein yaad aayengi tumhein?)",
            "Hum sabhi log us neta ko pasand nahi karte. (We all people don’t like that leader.)", "Mere bhai ne kisi ladki ko pareshan nahi kiya. (My brother didn’t bother any girl.)",
            "Usne dekhne ki koshish nahi ki. (He didn’t try to see.)", "Vo adhiktar Seeta ke saath khelta hai. (He mostly plays with Seeta.)",
            "Usne mere liye kabhi kuch kiya? (Did he ever do anything for me?)", "Usne kuch nahi kiya. (He didn’t do anything.)", "Hum tumhare saath ghoomne jayenge. (We will go for a walk with you.)",
            "Ram use bahut pyar karta hai. (Ram loves him/her a lot.)", "Tumne mera dil dukhaya hai. (You have hurt me.)", "Aankhen dhokha deti hai. (Eyes are deceptive.)",
            "Kya tumne sabhi ko khana paros diya hai? (Have you served the food to all?)", "Kisne tumhen dekha tha? (Who had seen you?)", "Ye dil pyar ke liye tadapta hai. (This heart craves for love.)",
            "Maine bhagvaan se kuch manga hai. (I have begged something from God.)", "Usne galti ki hai. (He has committed / made a mistake.)",
            "Hamari aankhen use dekh rahi thi. (Our eyes were searching him.)", "Kisne tumhein chot pahunchai? (Who hurt you?)", "Maine aisa kabhi nahi socha. (I never thought so.)",
            "Ram ne mujhe meri jeet par badhai di hai. (Ram has congratulated me on my victory.)", "Dukandaar ne mujhe loot liya. (Shopkeeper ripped me off.)",
            "Kya tum mujhe yaad dilaoge? (Will you remind me?)", "Bure daur mein tumhara saath kisne diya? (Who supported you in bad phase?)", "Maa badle mein kuch nahi mangatee.(Mother demands nothing in return.)",
            "Hum 2:00 baje park mein ghoom rahe the. (Vo kiske liye itni door gaya? (For whom did he go this far?)", "Tumhara chehra mujhe kisee ki yaad dilata hai. (Your face reminds me of someone.)",
            "Ye kaun karta hai? (Who does it?)", "Ye saap sadiyon se apne saathi ki talash kar raha hai. (This snake has been searching his partner for centuries.)",
            "Usne kitaab le lee thi. (He had taken the book.)", "Main pen se likhoonga. (I will write with a pen.)", "Kya tum roz Hanuman jee kee pooja nahi karti? (Do you not worship Lord Hanuman daily?)",
            "Vo ghoomne gaya tha. (He had gone for a walk.)", "Ye kaam kisne kiya? (Who did this work?)", "Ye kahaniyan mujhe pasand nahi. (I don’t like these stories.)",
            "Humne galti nahi ki. (We didn’t make a mistake.)", "Ram mere bare mein kuch na kuch to batayega. (Ram will tell at least something about me.)",
            "Vo dono hame kya sikhaenge? (What will they both teach us?)", "Tum sabhya lagate ho. (You look civilized.)", "Vo tumhare ghar pahle hi aa chuka hai. (He has already come your home.)",
            "Main tumhein roz dekhne aaya. (I came to see you daily.)", "Tum apni galti sveekaar kar chuke ho. (You have accepted your fault.)", "Main tumse kabhi nahi mila. (I never met you.)",
            "Main kisi tarah office pahuncha hoon.(I have somehow reached office.)", "Kya aap humse kuch kah rahe hai? (Are you saying something to us?)",
            "Vo tumhare ghar ki taraf kyon aata hai? (Why does he come towards your home?)", "Main deevaar ke peeche chup gaya. (I hid behind the wall.)",
            "Main tumse milne ko pagal ho raha tha. (I was craving to meet you./I was being/getting crazy to meet you.)", "Main jald hi office pahunch raha hoon. (I am reaching office soon.)",
            "Seeta ne company chhodi aur chali gayee. (Seeta left the company and went.)", "Tum ache lag rahe ho. (You are looking good.)", "Tumne aisa kyon socha? (Why did you think so?)",
            "Usne gana kaha gaaya? (Where did he sing the song?)", "Priya mere dil ko samajh rahi thi. (Priya was understanding my feelings.)",
            "Maine sab kuch samjh liya tha. (I had understood everything.)", "Log mithaiyaan kha chuke the. (People had eaten the sweets.)",
            "Mujhe uski yaad aa rahi thi. (I miss her)", "Vah do saal pahle company chhod chuka hai. (He has left the company 2 years ago.)",
            "Vo tumhe dhamkee kyon deta hai? (Why does he threaten you?)", "Main office ke bahar pahle hi dekh chuka hoon. (I have already seen outside the office.)",
            "Kya aap humse sahmat hai? (Do you agree with us?)", "Ram dhokha de raha tha. (Ram was cheating.)", "Vo admi mujhse nahi milta hai. (That man does not meet me.)",
            "Tota chhat par ro raha tha. (The parrot was crying on the terrace.)", "Vo tumhara intezaar 2:00 baje se kar raha tha. (He had been waiting for you since 2’o’clock.)",
            "Main tumhare bare mein kuch nahi soch raha hoon. (I am not thinking anything about you.)", "Main tumse baat karoonga. (I will talk to you.)",
            "Log mujhse milne nahi aayenge. (People will not come to meet me.)", "Ram computer seekhta hai. (Ram learns computer.)", "Tumhare baal jhad chuke the. (You suffered a hair fall.)",
            "Main apne mata pita ko vida kar chuka hoon. (I have seen off my parents.)", "2 ghante ho gaye, ghaav se khoon nikal raha hai. (Blood has been oozing from the wound for 2 hrs.)",
            "Main apne mata-pita ko bhej chuka tha. (I had sent my parents.)", "Kya aap 3 saal se kaam kar rahe hai? (Have you been working for 3 years?)",
            "Kya aap subah se ghoom rahe hai? (Have you been walking since morning?)", "Nal dikhne mein achha lag raha tha. (The tap was looking good.)",
            "Usne tumhein dhamkee kyo dee thee? (Why had he threatened you?)", "Tum mujhe subah se kyo dhundh rahe ho? (Why have you been searching me since morning?)",
            "Ram office kab jata hai? (When does Ram go to office?)", "Usne mujhe akela chhod diya hai. (He has left me alone.)", "Tum aisa kyon sochte ho? (Why do you think so?)",
            "Main aapke paas kab aaya? (When did I come to you?)", "Main tumse kabhi nahi milunga. (I will never meet you.)", "kya apne khana khaya? (Did you eat the food?)",
            "Vo bhagvaan ki pooja kab se kr rahi hai? (Since when has she been worshipping God?)", "Tumne haal hi mein kaun si film dekhi hai? (Which movie have you recently seen?)",
            "Main ghoomne gaya. (I went for a walk.)", "Main 2 hour se thand se kaamp raha hoon. (I have been shivering with cold for 2 hours.)", "Vah 2 saal pahle company chhod chuka hai. (He has left the company 2 years ago.)",
            "Tumne mera mood kharaab kiya. (You spoiled my mood.)", "Mein 2008 se company me kaam kr rha hu. (I have been working with the company since 2008.)", "Kya Seeta vaha baithee? (Did Seeta sit there?)",
            "Vo tumhare ghar me khana kha chuka hai? (He has eaten the food at your home.)", "Jab main aaoonga, tum ghar ja rahe honge. (When I come, you will be going home.)", "Usne mujhe maaf kiya. (He forgave me.)",
            "Main office parisar mein cigarette nahi peeta. (I don’t smoke in office premises.)", "Main apni galtee mahasoos kar chuka hoon. (I have realized my mistake.)", "Maine ye paheli suljha di. (I unravelled this enigma.)",
            "Main kabhi gaana nahi gata. (I never sing a song.)", "Main aapke pass kab aaunga? (When will I come to you?)", "Mera bhai kisi tarah ghar pahuncha. (My brother somehow reached home.)",
            "Yah sunne mein acha lagta hai. (It sounds good.)", "Kya seeta vaha baithegee? (Will seeta sit there?)", "Sachin ne purane sabhi records tode. (Sachin broke all the previous records.)",
            "Main apka prastaav sveekaar karta hoon. (I accept your proposal.)", "Ram agle mahine computer seekhega. (Ram will learn computer next month.)",
            "Kya Seeta vaha baithatee hai? (Does Seeta sit there?)", "Tum aise kyon soch rahe ho? (Why are you thinking so?)", "Tum ache lagte ho. (You look good.)", "Tum itni saree kitabein kaise laoge? (How will you bring these many books?)",
            "Usne shadyantra ka bhanda phod diya. (He unearthed the conspiracy.)", "Maine tumhare liye prarthana ki thi. (I had prayed for you.)",
            "Ram achchhee tarah se taiyaree kar chuka hai. (Ram has prepared well.)", "Ye kisne kiya? (Who did it?)", "Usne anjane mein mujhe dukh pahuchaya. (He unknowingly hurt me.)",
            "Maine logo se paise ikatthe kar liye hai. (I have collected the money from people.)", "Main kasam khata hoon ki main Vaha kabhi nahi jaoonga. (I swear that I will never go there.)",
            "Tumhari kitaab kisne li? (Who took your book?)", "Kya hua? (What happened?)", "Tumhare dimaag ko kisne pareshan kiya? (What disturbed your mind?)",
            "Hum bevajah ek dusre se lad rahe the. (We were unnecessarily fighting with each other.)", "Tum kya dhoondh rahe ho?(What are you looking for?)",
            "Maine uski awaaz pehchan li. (I recognized his voice.)", "Usne mujhe poori tarah se sahayog ka aashvaasan diya. (He assured me of full cooperation.)",
            "Kya tumme se koai pooja karte hain? (Do any of you worship?)", " Usne itne sare logo ko kaise sambhala? (How did he handle these many people?)",
            "Jo koi mere pass aaya, maine madad ki. (Whoever came to me, I helped.)", "Khel ke dauraan vo khoya khoya sa laga. (During the game, he seemed to be lost.)",
            "Tum kaheen khoye se lagte ho. (You seem to be lost somewhere.)", "Maine use utana paisa nahi diya. (I didn’t give him that much money.)",
            "Hajaron log sadak par vidroh kar rahe hai. (Thousands of people are protesting on roads.)", "Yeh dukaan Ravivar ko chhodkar sabhi din khulti hai. (This shop opens every day except Sunday.)",
            "Tumhre alawa Me kisi ko bhi daant skta hu. (I can scold anyone except you.)", "(Mere pas Samsung ke alawa kai companies ke mobile hai. (Except Samsung, I have mobiles of many companies.)",
            "Ise chhodkar mujhe kuch bhi de do. (Give me anything except it.)", "Iske atirikt kuch aur khate ho kya? (Do you eat anything else besides this?)",
            "Is pen ke alawa mere pas do pen hai.(Besides this pen, I have two more pens.)", "Aman ke alawa mere pas Ashish bhi to hai. (Besides Aman, I have Ashish as well.)",
            "Tumhare atirikt mere pas kaun hai? (Who do I have besides you?)", "Chhuttiyon ke dauran Main Delhi mein tha. (I was in Delhi during the vacation.)",
            "Hum break ke dauran Sachin se mil sakte h. (We can meet Sachin during the break.)", "Maine padhai ke dauran naukari ki. (I worked during studies.)",
            "We Delhi ke daure ke dauran aaye. (He came during the visit of Delhi.)", "Main 9 baje tak kam karunga. (I will work till 9.)", "Hum somvaar tak thahare. (We stayed till Monday.)",
            "Main shanivaar tak wahan tha. (I was there till Saturday.)", "Main 2009 tak wahan tha. (I was there till 2009.)", "Main January tak wahan tha. (I was there till January.)",
            "Main wahan somvaar tak hun. (I am here till Monday.)", "Main 2013 tak Delhi mein raha. (I lived in Delhi till 2013.)", "Main 5 baje tak ghar par tha. (He was at home till 5.)",
            "Usne 3 baje tak TV dekha. (He watched TV till 3 o’clock.)", "Main bas Dehradun tak gaya. (I just went up to Dehradun.)", "Seeta 5 kilometer tak daudi. (Seeta ran up to 5 kilometers.)",
            "Tumhe 18 ki umar tak mobile nahi rakhna chahiye. (You should not keep mobile till 18.)", "Ye aap par nirbhar hai ki aap jayein ya nahi (It’s up to you whether you go or not.)",
            "Ye sab aap par hai, aap dekh lijiye. (It’s all up to you.)", "Wo is job ke layak nahi hai. (He is not up to this job.)", "Uska pradarshan ummid ke mutabik nahi h. (His performance is not up to the mark.)",
            "Log mere chaaron or the, fir bhi Main tanha tha. (People were there around me, yet I was lonely.)", "Prithvi surya ke charo or ghum rahi hai. (The Earth is revolving around the Sun.)",
            "Mummy aas paas nahi dikh rahi hai. (Mom is not being seen around.)", "Tum aas paas hi rahna, mujhe tumhari zarurat pad sakti hai. (You stick around! I may need you.)",
            "Kya tum saath aaoge? (Will you come along?)", "Tumhe yeh pen mobile ke saath milega. (You will get this pen along the mobile.)",
            "Mera office gali ke bagal mein hai. (My office is along the street.)", "Nadi ke saath saath ek road hai. (There is a road along the river.)",
            "Road ke kinare ek truck khada hai. (A truck is parked alongside the road.)", "Kya tum nadi tair kar paar kar sakte ho? (Can you swim across the river?)",
            "Ek aadmi sadak ke par khada tha. (There was a person standing across the road.)", "Mere dukaan sadak ke us par hai. (My shop is across the road.)",
            "Lota ludakte ludakte ludak gaya. (The metal pot (vessel) kept rolling and fell down.)", "Lota ludakte ludakte girne se bach gaya. (The metal pot kept rolling but escaped falling.)",
            "Main girne se bach gaya.(I escaped falling.)", "Main girte girte bacha. (I narrowly escaped falling.)", "Wo swimming pul me girte girte bach gaya. (He narrowly escaped falling into the swimming pool.)",
            "Wo cycle se girte girte bacha. (He narrowly escaped falling off the bicycle.)", "Vo paas hote hote rah gaya. (He narrowly missed the pass marks.)",
            "Glass mere haath se slip ho gaya or ludakne laga. (The glass slipped out of my hands and started rolling.)", "Ye mere dimaag se nikal gaya. (It slipped out of my mind.)",
            "Mujhe dhyan nahi raha. (It slipped from my mind.)", "Mujhe Sabziyan bhi laani thi. Dimag se nikal gaya. (I had to bring vegetables too. It just slipped out of my mind.)",
            "Chinta mat karo. Maine tumhare liye 2 kele bachakar rakhe hain. (Don’t worry. I have spared 2 bananas for you.)", "Chala jaye? (Shall we go?)",
            "Khaaya jaye? (Shall we eat?)", "Main 13 June ko us shahar me fans gaya tha. (I had been stuck in that city on 13th of June.)",
            "Mai chahkar bhi usse mil nahi saka. (I wished to meet her but I couldn’t.)", "Pani ab garam hai. Aap nahane jaiye. (Water is warm now. You go and take a bath.)",
            "Pani gunguna ho gaya hai. (The water has turned lukewarm.)", "Mene kisi ka kya bigada hai? (What wrong have I done to anyone?)",
            "Tumne mera kya bigada hai? (What wrong have you done to me?)", "Mene uska kya bigada hai? (What wrong have I done to him?)",
            "Aisa mere saath hi kyo hota hai? (Why does it happen only with me?)", "Main tair ke nadi par kar sakata hun. (I can swim across the river.)",
            "kya aap school ja paye? (Could you go to school?)", "kya vo ghar aa paya? (Could he come home?)", "aap mere ghar aa sakte the. (You could have come my home.)",
            "aapako apane doston se bat karani chaahie. (You Should talk to your friends.)", "aapko mujhe phone nahin karna chahiye. (You shouldn’t call me.)",
            "mujhe vahan jarur jana chaahie. (I must go there.)", "Mera ghar sadak ke us paar hai. (My house is across the road.)", "Wo sadak ke paar khada hai. (He is standing ­across the road.)",
            "hamen apne desh ke lie ladana chahiye. (We ought to fight for our nation.)", "use apne desh ke liye ladna chahiye tha. (He ought to have fought for his nation.)",
            "kya main rahul se bat kar sakta hun? (Could I talk to Rahul please?)", "kya main apke sath baith sakta hun. (May I sit with you?)",
            "rahul ko jana hai. (Rahul has to go.)", "Rahul ko nahin jana hai. (Rahul has not to go.)", "kya rahul ko jana hai (Has Rahul to go?)",
            "kya rahul ko nahin jana hai (Has Rahul not to go?)", "ye ek pakki sadak hai. (This is a metalled road.)", "ye ek kachchi sadak hai. (This is an unmetalled road.)",
            "aap mere kaun ho? (Who are you to me?)", "kash main ek ladaki hota (I wish I were a girl!)", "kash main ek guitarist hota (I wish I were a guitarist!)",
            "kash mere pas paise hote (I wish I had money!)", "kash main vahan ja sakata (I wish I could go there!)", "aisa sochana bhi mat. (Don’t even think so.)",
            "ye karana galat hai. (This is wrong to do it.)", "baarish hone do. (Let it rain.)", "Mere per me dard hai. (There is a pain in my leg.)",
            "Meri ungli me dard hai. (There is a pain in my finger.)", "Andar se chair le ke aao. (Bring the chair from inside.)", "Chair andar rakho. (Keep the chair inside.)",
            "Tumhe dant padegi. (You will be scolded.)", "Main mobile bahut chalata hun. (I use mobile a lot.)", "Main laptop bahut chalata hun. (I work on laptop a lot.)",
            "Mai bike bahut chalata hu. (I ride the bike a lot.)", "mainne jute utare. (I took off the shoes.)", "badh ne tabahi  macha di. (The flood created havoc.)",
            "unhonne vahan tabahi  macha di. (They created havoc there.)", "pure din main vyast tha. (Throughout the day, I was busy.)", "darvaje ko aadha khula chhod do. (Leave the door ajar.)",
            "main kathputli nahi hun. (I am not a puppet.)", "ye pyar nhi aakarshan hai. (It’s not love but infatuation.)", "vo bahut gahri nind me hai. (He is having a sound sleep.)",
            "sumit yahan hai. (Sumit is here.)", "sumit yahi par hai. (Sumit is very much here.)", "yah gairkanooni kaam hai (This is an illegal act.)",
            "buri aadatein chhod do (Give up bad habits.)", "kya tum tair sakate ho? (Can you swim?)", "Is glass ko mat todo. (Don’t break this glass.)",
            "ve phool tod rahe the (They were plucking flowers.)", "kisi ko gali mat do (Don’t abuse anybody.)", "aap mere gawah hain (You are my witness.)",
            "yah kanoon ke viruddh hai (It’s against the law.)", "kya ye fool murjhate nahi? (Do these flowers not fade?)", "Aap joote polish krte ho na (You polish shoes, right?)",
            "aapki meharbani hai (So kind of you.)", "bahut khushi se! (With great pleasure!)", "Mujhe galat na samajhein. (Don’t take me wrong.)",
            "Main bahut aabhari hun (I am highly obliged.)", "uski aatma ko shaanti mile! (May his soul rest in peace!)", "Main roz ek seb khata hun (I eat an apple daily.)",
            "Kaun nahi jata hai? (Who doesn’t go?)", "Kaun kaun nahi jate hain? (Who all don’t go?)", "Ram kabhi nahi khelta.(Ram never plays.)",
            "vah mere saath padhta tha (He was my class-fellow)", "krpaya ek glass paani laaie (Please bring a glass of water.)", "koi aapko bula raha hai (Somebody is calling you.)",
            "aap bahut der laga rahe hai (You are taking too long.)", "aap kaha kaam karte hain? (Where do you work?)", "khushi to mujhe hui hai (Pleasure is all mine.)",
            "Saari galati aapki hai. (It’s all your fault.)", "Nal band kar do (Turn the tap off.)", "Nal khol do (Turn the tap on.)", "Lamp bujha do. (Put out the lamp.)",
            "Lamp jalaa do. (Light the lamp.)", "Abhi main vyast hu. (I’m busy at the moment.)", "Kaheen nazar na lage. (Touch wood!)", "Mene tumhari sifaarish kr di hai. (I have recommended you.)",
            "Kutte ne use fir kat liya hai. (Dog has bitten him again.)", "Kya dulha doctor hai? (Is the Groom a doctor?)", "Vo mera bahut kareebi hai. (He is very close to me.)",
            "Meri baat suno. (Listen to me.)", "bachchon ko tang mat karo (Don’t tease the children.)", "Kitna badal gaya yar tu. (How changed you are!)",
            "Muje tyohar pasand hai. (I love festivals.)", "Baraat abhi nahi ayi. (Baraat hasn’t come yet.)", "Pagal ho gaya hai kya tu? (Are you mad or what?)",
            "dhyanapurvak suno (Listen carefully.)", "pharsh saaf karo (Clean the floor.)", "apni kameez utaar do (Take off your shirt.)", "School ka programme khatam ho chukka hai. (School function is over.)",
            "apni kameez pahan lo. (Put on your shirt.)", "Use pahale bolne do. (Let him speak first.)", "turant taiyar ho jao (Be ready at once.)",
            "Aage se dhyan rakhna. (Keep in mind from now.)", "bahut der ho gai hai (It’s very late.)", "apane daant saaf karo (Clean your teeth.)",
            "kya aapke hosh thikane hain? (Are you in your senses?)", "Yah ek zarurat ban gayi hai. (It has become a necessity.)", "Wo ajeeb tha. (He was awkward.)",
            "Yah kaisa kam karta hai? (How does it work?)", "Use apne aap sikhna hoga. (He’ll have to learn himself.)", "October tyoharon ka mahina hai. (October is a month of festivals.)",
            "Aur kuch hai aapke paas? (Anything else do you’ve?)", "Savdhan raho. (Be careful)", "Vah fisal kar gir gaya. (He slipped and fell down.)",
            "Vah kuyein me fisal kar gir gaya. (He slipped and fell into the well.)", "Zubaan fisal gai. (It was a slip of tongue.)", "Mom ghar par hain. (Mom is at home.)",
            "Mammi ghar par hi hain. (Mom is very much at home.)", "Vo thand se kanp raha tha. (He was shivering with cold.)", "Main class mein second aata hun. (I stand second in class.)",
            "Main vo hun jo aapke sath tha. (I am the one, who was with you.)", "Rahul vo hai jo mere sath tha. (Rahul is the one who was with me.)",
            "Main apne bachche ko nahla raha hun. (I am giving a bath to my son.)", "Aapne janbujh kar mera dil dukhaya. (You knowingly hurt me.)",
            "Rasoi se ek glass le ke aao. (Bring a glass from the kitchen.)", "Upar ke room se bottle le ke aao. (Bring the bottle from the room upstairs.)",
            "Mera man ho raha h ki me khana kha lu. (I am feeling like eating the food.)", "Mera man ho raha hai ki me naha lu. (I am feeling like taking a bath.)",
            "vo to apni publicity karega hi. (He will obviously do his publicity.)", "Main English bolte bolte atak jata hu. (I fumble while speaking English.)",
            "Isme mera koi haath nahi hai. (I am not involved in this.)", "Mai roz English sikhne ki koshish krta hu. (I try to learn English every day.)",
            "Seema bachche ko khila (play) rahi thi. (Seema was making the kid play.)", "Aisa Rahul ke saath hi kyo hota hai? (Why does it happen only with Rahul?)",
            "Tumne ye kahaa se uthayi thi? (Where had you picked it from?)", "Mujhse golmol baate mat kro. (Don’t talk clever with me.)", "Vo mujhe ghar ki dehliz paar nahi krne dega. (He will not let me enter the threshold of the house.)",
            "Suryanamskar kar lo or paani de do (Greet the Sun and offer the water.)", "Charger ko plug mein laga do (Plug the charger please.)", "Mujhe 5 rupaye vali toffe dedo (Please give me the toffee of Rs. 5 each.)",
            "Kaash mai vahaan hota. (I wish, I were there.)", "Ye main hi hun. (This is really me.)", "Usne hi to ye kaha tha. (It was he, who had said it)",
            "Usne mujhe mara tha vo bhi lathi se (He had beaten me, that too with a stick.)", "Aise mein ham kahan ja sakte hai (Where can we go in this situation?)",
            "Hamne aaj baahar dinner kiya. (We took the dinner outside today.)", "Tumhe apni galati maanni chahiye. (You should confess your fault.)",
            "Main naha raha hun. (I am taking a bath.)", "Main apne bete ko nahla raha hun. (I am giving a bath to my son.)",
            "Main padh raha hun. (I am studying / reading.)", "Main paddha raha hun. (I am teaching.)", "Main aapko English paddha raha hun. (I am teaching you English.)",
            "Main use sula raha hun. (I am making him to sleep.)", "Main hans raha hun. (I am laughing.)", "Main muskura raha hun. (I am smiling.)",
            "Main apko hasa raha hun. (I am making you laugh.)", "Meri vajah se aap muskura rahe hai. (I am making you smile.)", "Main samajh raha hun. (I can understand.)",
            "Main apko samajha raha hun. (I making you understand.)", "Ram mujhe samajha raha hai.(Ram is making me understand.)", "Hum unhe samajha rahe hain. (We are making them understand.)",
            "Vo ro raha hai. (He is weeping.)", "Wo bhavuk ho raha hai. (He is being/getting emotional.)", "Wo mujhe bhavuk kar raha hai. (He is making me emotional.)",
            "Main apko khush kar raha hun. (I am making you happy.)", "Maine apko khush kiya. (I made you happy.)", "Maine tumhen dukhi kiya. (I made you sad.)",
            "Aap mujhe dukhi kar rahe hain. (You are making me sad.)", "Main khush ho gaya. (I got happy.)", "Main khush ho raha hun. (I am getting happy.)",
            "Main dukhi ho gaya. (I got sad.)", "Main dukhi ho raha hun. (I am getting sad.)", "Main use khila raha hun.(I am making him eat.)",
            "Main apako (apne hatho se) khila raha hun.(I am feeding him.)", "Main apni gay ko khila raha hun.(I am feeding my cow. / I am serving my cow.)",
            "Main sabhi ko khila raha hun. (I am serving all.)", "Main sabhi ko khana de raha hun. (I am serving the food to all.)", "Main sabhi ko khana khilava raha hun. (I am getting the food served to all.)",
            "Main shirt pahan raha hun. (I am wearing the shirt.)", "Main shirt pahna raha hun. (I am making him wear the shirt.)", "Main apne chhote bhai ko shart pahna raha hun. (I am making my younger brother wear the shirt.)",
            "Main seekh raha hun. (I am learning.)", "Main tumhe sikha raha hun. (I am making you learn.)", "Glass ko ulta kar do. (Turn the glass upside down.)",
            "Maine book ko ulta kr diya. (I turned the book upside down.)", "Mene glass ko ulta rkh diya (I put / kept the glass upside down.)",
            "Mene book ko ulta rkh diya (I put/ kept the book upside down.)", "Glass ko sidha kar do. (Turn the glass right side up.)", "Book ko seedha kar do. (Turn the book right side up.)",
            "Tumne book ulti pakad rakhi h. (You have held the book upside down.)", "Aapne glass ko ulta pakad rakha hai. (You have held the glass upside down.)",
            "Aapne kap ulta pakad rakha hai. (You have held the cup upside down.)", "Aapki slippers ulti padi hui h. (Your slippers are lying upside down.)",
            "Apni chappal sidhi kar lo. (Turn your slippers right side up.)", "Mene shart ulti pahan li. (I put on the shirt inside out.)", "Shart ko sidha kar lo.(Turn the shirt right side out.)",
            "Usne meri baat ko ulta samajh liya. (He took me otherwise / wrong.)", "Ulta maine to vaha bahut mazaa kiya . (I rather enjoyed myself there.)",
            "Ulta vo mujhe daatne lage. (He rather started scolding me.)", "Usne mere kahe ka ulta kiya. (He did opposite of what I said.)", "Ulta ghumo. (Please turn opposite.)",
            "Vo ulta leta hua hai. (He is lying upside down.)", "Seedha leto. (65th Day {Sixty Fifth Day})", "Main bahut padhta tha taki achchhe number laa pau. (I used to study a lot so as to score good marks)",
            "Usne mera saath diya taaki main bhi uska saath du. (He supported me so as to receive my support too.)", "Usne meri baat ko jod tod kar aapke saamne rakha. (He manipulated my words and conveyed to you.)",
            "Aapne apni income or expenditure ko her fer karke apni performance ko behtar dikhaya hai. (You have manipulated your income and expenditure to show your performance better.)",
            "Ye aapke bachche ke liye anukool maahol nahi hai. (This is not a conducive environment for your child.)", "Roz exercise karna hamaare mind or body ke liye anukool hai. (To exercise everyday is conducive for our mind & body.)",
            "Aapka swabhav aur vyavhaar kaafi had tak aapki parvarish par nirbhar karta hai. (Your nature and behavior largely depend on your upbringing.)",
            "Unka paalan poshan vipreet paristhitiyo me hota hai, yahi unhe majboot bana deta hai. (Their upbringing takes place in adverse circumstances, that’s what makes them strong.)",
            "Bhool ke bhi company ke niyamo ko mat todna. (Don’t dare to breach company’s rules.)", "Bachcho ko aaspas khelte dekhna tension kam kar deta hai. (Seeing the kids playing around lessens one’s tension.)",
            "Usne apni mehnat se garibi ko amiri me badal diya. (He changed his adversity into prosperity with his hard work.)", "Vo ek aashavadi insan hai. Uski soch positive hai. (He is an optimistic person. His has a positive thinking.)",
            "Main apne achchhe bhavishya ke prati aashavadi hu. (I am optimistic to have a bright future.)", "Us aparadhi ke chhoot jaane se har koi hairan tha. (Everyone was shocked with that criminal’s acquittal.)",
            "Ek nirdosh vyakti ka sajaa se bach nikalna achchhi khabar hai. (Acquittal of an innocent person is good news.)", "Jab aap baahar nikalte hai, tab aapko duniyadari ka pata lagta hai. (When you go out, you get to know about life.)",
            "Ye kapde nikalkar maine apna bag halka kar diya hai. (I’ve lightened my bag by taking these clothes out.)", "Is bulb se usne apne kamre me ujala kar diya. (He lightened his room with this bulb.)",
            "Tumhara support use bure daur me majboot banata hai. (Your support strengthens him in adverse situations.)", "Ameeri dost banati hai aur garibi unhe parakhti hai. (Prosperity gains friends and adversity tries them.)",
            "Samridhi ka matlab hai paisa aur property. (Prosperity means to have money and property.)", "Vo ek nirashavadi insan hai. Uski soch negative hai. (He is a pessimistic person. He has a negative thinking.)",
            "Ye makaan 2002 me sarkar ke dwara apne kabze me liya gaya tha. (This house had been acquired by the government in 2002.)",
            "Main aapse milne ka besabri se intzaar kar raha hu. (I am looking forward to meeting you.)", "Aap is property ko kharidne ki umeed kar sakte hai. (You can look forward to buying this property.)",
            "(Maana aapke paas paise na ho. Ab aap kya karoge? (For instance, you have no money. What would you do now?)", "(Maana vaha koi na ho, aise main aap dar jaoge. (For instance, there is no one. You’d be scared then.)",
            "Jab aap baahar nikalte hai, tab aapko duniyadari ka pata lagta hai. (When you go out, you come to know about life.)", "Main roz daudne jaata hu. Jiski vajah se, mujhme achchha stamina hai. (I go for running every day. As a corollary to that, I have good stamina.)",
            "Gandagi wali jagaho me paani peena jokhim bhara hai. (To drink water at non hygienic places is a health hazard.)", "Zarurat se zyada iska prayog vaatavaran ke liye thik nahi hai. (Its excessive use is an environmental hazard.)",
            "Tumhein daanta jata hai. (You are scolded.)", "Humein peeta ja raha tha. (We were being beaten.)", "Mujhe ek kitaab dee jayegee. (I will be given a book.)",
            "Is puraskaar ke liye keval ek aadmee chuna jata hai. (Only one person is selected for this prize.)", "Mujhe tumhare bare mein bataya gaya hai. (I have been told about you.)",
            "Bharat ko shantipriya desh ke roop mein jana jata hai. (India is known as a peaceful nation.)", "Kya use bheja gaya? (Was he sent?)", "Tumhare liye special khana banaya ja raha hai. (A special food is being prepared for you.)",
            "Unke saath bura bartaav kiya gaya. (They were treated badly.)", "Use ek mahan yoddha ke roop mein jana jata tha. (He was known as a great warrior.)", "Ye likha ja chuka hai. (It has been written.)",
            "Kya use pyar kiya jata hai? (Is he loved?)", "film ke mega hit hone ki ghoshna ho chuki hai. (The movie has been declared a mega hit.)",
            "Gang ke mukhiya ko 2:00 baje market mein dekha gaya hai. (The chief of the gang has been seen in the market at 2.)", "Kya unhe is shrarat ke liye daanta gaya?. (Were they scolded for this mischief?)",
            "Dono doshiyo ki pahchan kee ja chukee hai. (Both the culprits have been identified.)", "Chai har subah pee jatee hai. (Tea is taken every morning.)", "Use kiske saath bheja gaya? (Whom was he sent with?)",
            "Kismat se mujhe vaha bheja gaya aur main tumse mila. (Fortunately, I was sent there and I met you.)", "Mujhe ek adhyapak ke roop me jana jata h. (I am known as a teacher.)",
            "Bachchon ko bahar bheja gaya. (Children were sent outside.)", "Use murder ke bare mein kyo nahi pochha jata? (Why is he not asked about the murder?)",
            "Mujhe iske bare mein soochit nahi kiya gaya. (I was not informed about this.)", "Kitabein chhapee ja rahee hai. (Books are being printed.)",
            "Use kab tak nahi puchha gaya? (Until when was he not asked?)", "Har jagah mithiyaan batee ja rahee hai. (Sweets are being distributed everywhere.)",
            "Waha ek building banai ja chuki hai. (A building has been constructed there.)", "Vo police ke dwara pakda gaya. (He was caught by the police.)",
            "Hamein aisa kuch nahi diya gaya hai. (We have not been given anything as such.)", "Hamein vaha faltu mein kyo bheja jata hai? (Why are we unnecessarily sent there?)",
            "Mujhe kahi bheja gaya. (I was sent somewhere.)", "Kya tumhe bataya gaya hai ki hum kaha h? (Have you been told where we are?)",
            "Use yah kyo diya gaya? (Why was he given this?)", "Hamein kya bataya gaya? (What were we told?)", "Unhein kaha bheja gaya tha?. (Where had they been sent?.",
            "Use puchha jana chaahiye tha. (He should have been asked.)", "Hamein bheja jana hai. (We have to be sent.)",
            "Ye kaam ho jana chahiye tha. (This work should have been done.)", "Mummy ko puchha jata tha. (Mom used to be asked.)", "Kya Ram ko kitaab dee ja sakti hai? (Can Ram be given the book?)",
            "Use kya diya gaya? (What was he given?)", "Ram ko kiske saath bheja jana chahiye? (With whom should Ram be sent?)",
            "(Hamein kya padhaya jata tha? (What were we taught?)", "Mujhe mobile diya ja sakta hai. (I can be given a mobile.)",
            "Agla prashn kya hona chahiye? (What should be the next question?)", "Tumhein saja milni chahiye. (You must be punished.)",
            "Hamein kharch ke liye milne chahiye. (We should be given pocket money.)", "Hamari tankhvah badhai jani hai. (Our salary is to be increased.)",
            "Usse puchha jana tha. (He had to be asked.)", "Homework jaroor karna chahiye. (Homework must be done.)",
            "Mere saath kisi ko bheja nahi jana chahie. (No one should be sent with me.)", "Use kaha bheja jana chahiye. (Where should he be sent?)",
            "uska naam kuch der baad pukara jana hai.  (His name is to be called after a while.)", "Mujhe padhne nahi diya jata. (I am not let study.)",
            "Tumhein ye nahi diya jana. (It’s not be given to you.)", "Ye pani piya nahi ja sakta kyoki yah khara hai. (This water is not drinkable as it is saline.)",
            "ek vidyarthee ko uske sahpathee dwara pratadit kiya gaya. (A student was tortured by his classmate.)", "Diwali khushi se manaee gai. (Diwali was celebrated with joy.)",
            "Vo mujhse likhvaata hai. (He makes me write.)", "Vo mujhe bhijvaata hai. (He makes me go.)", "Vo mujhse kaam karvati hai. (She makes me work.)",
            "Vo mujhse patra likhvatee hai. (She makes me write letters.)", "Vo mujhe bhijva rahi hai. (She is making me go.)", "Vah mujhe bevakuf bana rahi hai. (She is making me fool.)",
            "Usne mujhe samjhaya. (She made me understand.)", "Main tumse kaam karvaoonga. (I will make you work.)", "Tum mujhe usse ladva rahe ho. (You are making me fight him.)",
            "Bachche mujhe bevakuf bana rahe hain. (Kids are making me fool.)", "Tum hamein hasate the. (You made us laugh.)", "Main tumhein jane de sakta tha. (I could have let you go.)",
            "Ram ye kisi se bhi karva sakta hai. (Ram can get it done by anyone.)", "Main tumhe khaane ko kuch dilwa skta hu. (I can get you something to eat.)",
            "Ram mujhe panee dila sakta hai. (Ram can get me water.)", "Kya tum mujhe exam pass karva skte ho? (Can you make me pass the exam?)",
            "Main tumhein pass karva sakta hoon. (I can get you pass.)", "Paisa tumhein pyar nahi dilva sakta. (Money can’t get you love.)",
            "Tum har kisi ko khush nahi kar sakte. (You can’t make everyone happy.)", "Main tumse nahi khareedva sakta. (I can’t make you purchase.)",
            "Kya vo use hasa saka? (Could he make her laugh?)", "Tumhein ye kaam karvaana padega. (You will have to get it done.)", "Vo mujhe burger khilata hai. (He gets me Burger.)",
            "Vo mujhe acha khana khilata hai. (He gets me good food.)", "Tum mujhe America kab bhijvaoge? (When will you make me go America?)",
            "Ram hamein panee pilva raha tha. (When will you get me go America.)", "Us bachche ne har kisi ko hasaaya. (That child made everyone laugh.)",
            "Tumne mujhe rulaya hai. (You have made me cry.)", "Vo comedian hai. Vo logo ko hasata hai. (He is a comedian. He makes people laugh.)",
            "Rahul mujhse apna homework karavayega. (Rahul will make me write his homework.)", "Tum usse apna homework kyo karvate ho? (Why do you make him write your homework?)",
            "Usne tumse kise call karvaee? (Whom did he make you call?)", "Usne ye pen tumhein kaise dilvaya? (How did he get you this pen?)",
            "Tumne mujhe pyar ki keemat samjhayee. (You made me understand the value of love.)", "Tum Ram ko usse nahi pitva sakte. (You can’t make him beat Ram.)",
            "Tum ram se use nahi pitva sakte. (You can’t make Ram beat him.)", "Main tumhein samjha nahi sakta. (I can’t make you understand.)",
            "Maine tumse baltee bharvai. (I made you fill the bucket.)", "Usne tumse paise kyo kharch karvaye? (Why did he make you spend the money?)",
            "Main kya karvata hoon? (What do I get done?)", "Maine baal katavaye. (I got the hair cut.)", "Rahul yahi se baal katvata hai. (Rahul gets the hair cut from here itself.)",
            "Kya tum baal katvaoge? (Will you get the hair cut?)", "Tumne baal kaha se katvaye? (Where did you get the hair cut from?)",
            "Ram tumse ye kaam karvayega. (Ram will make you do it.)", "Mujhe ye kaam karvana hai. (I have to get it done.)", "Hum Ram se gaana gava sakte hai. (We can make Ram sing the song.)",
            "Main tumhein kya samjha raha hoon? (What am I making you understand?)", "Vo mujhse apne 2 batch padhva raha hai. (He is making me teach his 2 batches.)",
            "Humne use bahar bhijvaya. (We made him go out.)", "Tum ye kisse karvaoge? (Whom will you get this done from?)", "Tumne mujhse jhooth bulvaya. (You made me tell a lie.)",
            "Ram mujhe roz rulata hai. (Ram makes me cry every day.)", "Tum kab tak ye kaam khatm karva doge? (By  when will you get this work finished?)",
            "Kya tum mujhe dance sikhva sakte ho? (Can you get me learn dance?)", "Tumne mujhe computer sikhaya/sikhvaya. (You made me learn computer.)",
            "Usne hamein coke pilaee/pilvaee. (He made us drink coke.)", "Rahul ne hamein exercise karvaee. (Rahul made us do the exercise.)",
            "Tumne mujhe pitvaya. (You got me beaten.)", "Rahul ne usse mujhe kuch paise dilvaye. (Rahul got me some money from him.)",
            "Main ye kaam kaise karva sakta hoon? (How can I get this work done?)", "Humne use bhijvaya par usne vaha kaam nahi kiya. (We made him go but he didn’t work there.)",
            "Log jabardastee tumhein vaha bhijvayenge. (People will forcefully make you go there.)", "ve tumhen america bhijvayenge basharte tum unhen saabit karo ki tum sabse ache ho. (They will make you go America provided you prove them to be the best.)",
            "Bobby ne mujhe achchha khana khilaya, jo uski mummy ne pakaya tha. (Bobby made me eat the delicious food, cooked by his mom.)", "Maine har kisi ko us hotel mein khana khilvaya. (I made everyone have food in that hotel.)",
            "Use mujhe ahsaas dilvaana hoga ki vo sabse acha hai. (He will have to make me realize that he is the best.)", "Mere papa ne mujhe mahsoos karvaaya ki hamein jarurat mandon ki madad karni chaahiye. (My father made me realize that we should help needy people.)",
            "Insaaniyat naam ki bhi koi cheez hoti hai (There is something called humanity too.)", "Mera kapda sikud gaya hai (My cloth has shrunk.)",
            "Maine ek baar mein 2 got daali carrom khel mein (I potted 2 pieces in one go in carrom.)", "Shutter ko upar dhakelo (Push the shutter upwards.)",
            "Baalti mein paani bhar do nal se (Fill the bucket with water using the tap.)", "Baat ko hansi mein mat udao. (Don’t laugh away the matter.)",
            "Dhakkan laga do. (Please put the lid back.)", "shakl par mat jao sirat par jao . (Don’t judge by face but virtues.)", "Uski shakl par mat jao vah masoom nahi hai. (Don’t judge him by his face, he is not innocent.)",
            "Meri hansi chhoot gayi (I couldn’t hold laughing.)", "Maine use apni bike par ghumaya (I made her visit the city on my bike.)",
            "Main yahaan bahut umeed ke saath aaya hu (I have come here with lots of hope.)", "Aaj main vahaan jaane ki sthiti mein nahi hu (I am not in position to go there today.)",
            "Tum mera kuchh nahi bigaad sakte (You can’t do anything wrong to me.)", "Mera ek ek minute lakh ke barabar hai (My time is extremely precious.)",
            "Mere haath ki chamadi sikud gayi (The skin of my hands is wrinkled.)", "Uske chakkar mein mat pado (Don’t rely on him.)", "Main usko pakdne ke chakkar mein hu (I am planning to catch him.)",
            "Uski gardan shareer se alag thi (His head was away off the body.)", "Ye kati hui patang maine luti hai (I caught this cut kite.)",
            "Aapne mujhe bulaya? (Have you asked for me?)", "Vah mujhe peetne ke chakkar me yaha aya. (He came here to beat me.)", "Kya maine tumhara paksh nahi liya? (Did I not favour you?)",
            "Agar ye fake nikla to mujhse bura koi nahi hoga (If it is found fake, I will not spare you.)", "Ham har roz nayi cheeje seekhte hai. (We learn new things every day.)",
            "Uski stithi kharab ho rahi hai. (Her condition is worsening.)", "Wah khidki khulne ka intezar kar raha hai. (He is waiting for the window to open.)",
            "Tum ekdam thik bol rahe ho. (You are absolutely right.)", "Usne jhuton ko saja di. (He punished the liars.)", "Ap isko ek taraf rakho. (Please keep it aside.)",
            "Mujhe aksar vahaan jaana hota hai (I have to often go there.)", "Sansaar me gyan ki kami nahi hai. (There is no dearth of knowledge in this world.)",
            "main apke sath dance karna chahunga. (I would like to dance with you.)", "main apke sath baithna chahunga. (I would like to sit with you.)",
            "main apke sath bat nahin karna chahunga. (I would not like to talk to you.)", "kya ap mujhse bat karna chahenge? (Would you like to talk to me?)",
            "kya ap mujhse milna chahenge? (Would you like to meet me?)", "ap mujhse kahan milna chahenge? (Where would you like to meet me?)",
            "aap kya khana chahenge? (What would you like to eat?)", "aap kya karna chahenge? (What would you like to do?)",
            "kaise aana hua ?  (What brings you here?)", "main zuban ka pakka hun. (I am a person of words.)", "main bahut kharch karata hun. (I spend a lot.)",
            "mujhe aajakal paise ki dikkat hai. (I have financial problem these days.)", "usaki ek aankh kharab hai. (He is blind of one eye.)",
            "ye ek achchha bahana hai. (This is a good excuse.)", "vo bahut batuni hai. (She is very talkactive.)", "tum mere hamnam ho. (You are my namesake.)",
            "tumne kaha tha tum hamesha mera saath doga. (You had told me, you will always stand by me.)", " kya main vajah jaan sakata hun? (May I know the reason?)",
            "tumane use galat samajha. (You misunderstood him.)", "vo sare din khali tha. (He was idle all the time.)", "mera chhota bhai apani marzi ka malik hai. (My younger brother has his own ways.)",
            "main apane man ki karata hun. (I have my own ways.)", "mere hath mein dard tha. (I had a pain in my hand.)", "kya isase koi phark padata hai ? (Does it make any difference?)",
            "kya isase koi ph (ark padata hai? (Does it make any difference?)", "tumhe delhi  kaisi lagi? (How did you find/like Delhi?)",
            "main shahar se bahar tha. (I was out of station.)", "mere paas car hai. (I have got a car.)", " (usake paas  computer hai.)",
            "chhod na. (Let it be.)", "ya bat khatm karo. ya bhul jao. (Leave it.)", " (Forget it.)", "usaki tabiyat thik nahi hai. (He is unwell.)",
            "kal mera eksident (durghatana) ho gaya. (I met with an accident yesterday.)", "tumane mujhe ek rupaye zyada de diya hai. (You have given me a rupee extra.)",
            "ye kapada ghatiya quality ka hai. (This cloth is of inferior quality.)", "asaman badalon se ghira hua hai. (The sky is full of clouds.)",
            "tum apani umr se kam lagate ho. (You look younger than your age.)", "vo apani umr se zyada lagata hai. (He looks older than his age.)",
            "mujhe nind aa rahi hai. (I am feeling sleepy.)", "mujhe bhukh lag rahi hai. (I am feeling appetite.)", "mujhe bukhar sa lag raha hai. (I am feeling feverish.)",
            "tumhe apane aap par sharm ani chahie. (You should be ashamed of yourself.)", "aapaki  padhai kaisi  chal rahi  hai ? (How is your study going on? )",
            "usake dimag mein kya chal raha hai ? (What is going on in his mind?)", "vahan kya chal raha hai ? (What is going on there?)", "kya vahan kuchh chal raha hai ? (Is something going on there?)",
            "vo mere dilo dimag par bas gayi hai. (She has got on to my heart and soul.)", "yah aam rasta nahin hai. (This is not a thoroughfare.)",
            "ye charcha mein hai. (It is in talk.)", "vo ek kukhyat vyakti hai. (He is a notorious person.)", "Road repairing ki vajah se band hai. (The road is closed for repairs.)",
            "mobile repairing ke lie uske paas tha. (The mobile was with him for repairs.)", "yah ek afwah hai. (This is a rumour.)", "doshi  kon hai ?  (Who is to blame?)",
            "kya tum sahi  ho ? (Are you in the right?)", "kya tum dayin taraf  ho ? (Are you on the right?)", "kya tum galat ho ? (Are you in the wrong?)",
            "kya tum bayin taraf ho ? (Are you on the left?)", "kya tum hosh mein ho ? (Are you in your senses?)", "kya tum mujhase darate ho ? (Are you scared of me?)",
            "mujhe sirdard ho raha tha. (I had a headache.)", "main ekad mahine mein aa jaunga. (I will come in a month or so.)", "ve ekad sal mein landan jayenge. (They will go London in a year or so.)",
            "vo ekad din mein aayenge. (He will come in a day or so.)", "mera pet dard ho raha tha. (I had a stomachache.)",
            "mere pair mein dard tha. (I had a pain in my leg.)", "andhera ho raha hai. (It is getting dark.)", "lamp mein tel nahin hai. (The lamp has no oil.)",
            "hamare desh mein anaj ki kami nahin hai. (There is no shortage of food grains in our country.)", "ek kahavat hai, asambhav kuchh bhi nahi (There is a saying that ‘nothing is impossible’.)",
            "is pareshani se chhutakara paane ka kya koi tareeka hai ? (Is there a way to get out of this problem?)", "ye rahi aapaki ghadi./ye leejie aapaki ghadi. (Here is your watch.)",
            "ye lijie paise. (Here is the money.)", "ye lijie pain. (Here is the pen.)", "lo, teachar aa gaye. (Here comes the teacher.)",
            "lo, rohit aa gaya . (Here comes Rohit.)", "lo, rohit aa gaya. (Here comes Rohit.)", "lejie, aap aa gaye.  (Here comes you.)",
            "ye lo, main aa gaya . (Here comes I.)", "tum kab se yahan ho ?  (Since when have you been here?)", "ye mere kabu se bahar hai. (This is out of my control/hands.)",
            "ghar jal raha hai. (The house is on fire.)", "bhagavan ka aashirvad hamesha tumhare saath hai. (God’s grace is always with you.)",
            "dusaro ki  nakal mat karo. (Don’t copy others.)", "pensil se mat likho. (Don’t write with a pencil.)", "aag jail rahane do. (Keep the fire on.)",
            "hamen buri  aadaten chhod deni  chaahie. (We should give up bad habits.)", "khana achchhi  tarah chabao. (Chew the food well.)", "naak saph karo. (Blow your nose.)",
            "kot ke batan band karo. (Button up the coat.)", "is mobail ko dono hathon se pakado. (Hold this mobile with both hands.)", "pura din main ram ka intazar kar raha tha. (All the while I was waiting for Ram.)",
            "lagta hai, vo tumhara koi hai. (Looks as, he is someone to you.)", "lagta hai, hamare adhyapak aaj yahan nahi hain. (Looks as, our teacher is not here today.)",
            "lagta hai, tumhari  tabiyat thइk nahi hai aaj. (Looks as, you are not well today.)", "vo hamesha ki  tarah mere paas aaya. (He came to me as usual.)",
            "papa ne hamesha ki tarah use danta aur vo ghar se chala gaya. (As usual, Dad scolded him and he left home.)", "chahe jo ho, ham aisa nahin karenge (Come what may, we will not do so.)",
            "chahe jo ho, main vahan jaunga. (No matter what, I’ll go there.)", "bhagavan jane vo kon hai.  (God knows who he is.)", "bhagavan jane ve kahan hain. (God knows where they are.)",
            "bhagavan jaane ye kisane banaya. (God knows who made this.)", "sardiyaan ane vali hain. (The winters are round the corner.)",
            "Dad ekad ghante me pahunchane wale h. (Dad is about to reach in an hour or so.)", "main apna samay kat raha hun. (I am whiling away my time.)",
            "tum kitne saal ke ho? (How old are you?)", "main ye roz-roz khakar thak gaya hun (I am tired of eating it daily.)", "main tumase pak gaya hun. (I am tired of you.)",
            "vo is samay kanapur ke aasapas hoga (He will be hereabouts Kanpur this time.)", "mujhe vishvas hai ki main safal ho jaunga. (I am sure of success.)",
            "dhyan rakhna tumhen vahan jana hai (Keep in mind that you have to go there.)", " tumane meri bahut madad ki hai (You have been a great help to me.)",
            "ham is gift ki ahmiyat ko nahi tol sakte. (We can’t assess the value of this gift.)", "vo bahut dur se aa raha hai. (He is coming from afar.)",
            "main 18 saal ka ho gaya hun (I have turned 18.)", "vo15 saal ka ho gaya hai. (He has turned 15.)", "maine kadmon ki avaz suni thi (I heard a footfall.)",
            "vo sare din bahut chup-chup sa laga (He seemed very aloof all the time.)", "mujhe bachchon ka tutlana pasand hai (I like the lisping of children.)",
            "maine ummid ke mutabik kam nahi kiya (I didn’t work up to the par.)", "yahi kitab to main chahta hun. (This is the very book I want.)",
            "vo bhai ki maut ki vajah se sadme me thi. (She was in a shock due to her brother’s death.)", "mujhe ye paheli sulajhani hai. (I have to unravel this enigma.)",
            "main hamesha tumhara saath dunga (I will always stand by you.)", "kya tumne hal hi me use dekha hai? (Have you seen him lately/recently?)",
            "Filhal tum yahan intajar karo. (For the time being, you wait here.)", "Filhal main tumhen ye kitaab de raha hun. (I am giving you this book for the time being.)",
            "Sumit aaspas nahi dikh raha hai (Sumit is not seen around.)", "Mom aaspas nahin dikh rahi thi (Mom was not seen around.)", "ve aaspas nahi dikh rahe hai. (They are not seen around.)",
            "chalo tahalate hai. (Let’s stroll.)", "ye kitaben mere kisi kam ki nahi (These books are of no use to me.)", "India Australia ke viruddh ladkhada raha hai (India is tottering against Australia.)",
            "kya tum mujh par ek ahasan karoge ? (Will you do me a favor?)", "maine uske sath kya galat kiya hai? (What wrong have I done to him?)",
            "hamare bich batchit nahi hai (We are not in speaking terms.)", "Hum ek dusre ke yahaan aate jaate nahi. (We are not in visiting terms.)",
            "vahan jana thik nahi hai. (It is not worthwhile going there.)", "tumhen apni galti man leni chahie (You should confess your fault.)",
            "main tumhen nahi chhodunga (I will not spare you.)", "sach kahun to main bahut khush hun (To be honest, I am very happy.)",
            "vo zindagi  aur maut ke bich jujh raha hai (He is hovering between life and death.)", "vo behosh ho gaya hai. (He has become unconscious.)",
            "tumhari shaadi karib aa rahi hai. (Your marriage is drawing near.)", "launching ki tarikh karib aa rahi hai. (The launching date is drawing near.)",
            "maine apni shart rakhi. (I laid my condition.)", "mujhe paise ki sakht zarurat hai. (I am in a dire need of money.)", "hamen dusaron ka apamaan nahi karana chahie. (We shouldn’t abase others.)",
            "hamesha ki tarah, yashi meri god mein aayi. (As always, Yashi came to my laps.)", "main tumhe dekhane ke lie tadap raha tha. (I was craving to see you.)",
            "tumhari jeb me  vo ubhara hua kya hai? (What’s that bulge in your pocket?)", "samudr mein vo ubhra hua kya hai? (What’s that bulge in the sea? )",
            "ghamand mujhe kabhi  chhu nahi sakata. (Arrogance can never caress me.)", "jus ko halke-halke piyo. (Sip the juice slowly.)",
            "batachit se karibi badhati hai. (Conversation increases the proximity.)", "use naukari se nikal diya gaya hai. (He has been fired/expelled from the job.)",
            "sabji bechane vala apko lut raha hai. (The vegetable seller is ripping you off.)", "tum uske kaan me kya fusfusa rahe ho? (What are you whispering in his ear?)",
            "ham uski harkat par nazar rakhenge. (We will keep a vigil on his activity.)", "ghaav se kon nikal raha hai. (The blood is oozing from the wound.)",
            "tum khali samay me kya karte ho?  (What do you do in your leisure time?)", "tum ye kitab hamesha ke lie rakh sakate ho. (You can keep this book for keeps.)",
            "kal rat mujhe bahut gahri nind aayi. (I had a sound sleep last night.)", "me kisi ke pichhe bhagna pasand nahi krta. (I don’t like to run after anyone.)",
            "usne ghav par marham lagaya. (He put ointment on the wound.)", "dahez pratha hamare samaz ke liye ek abhishap hai. (Dowry system is a malediction for our society.)",
            "aatankavadi hamle suraksha khamiyon ki vajah se hote hain. (Terrorist attacks occur due to the security lapse.)", "main 6 maheene ke bad usaka chehara dekh paya. (I could see his face after a lapse of six months.)",
            "main koi kasar nahi chhodunga. (I will leave no stone unturned.)", "vo sigret peene ka aadi ho gaya hai (He has been addicted to smoking.)",
            "isse mera kam chala jayega. (It will serve my purpose.)", "main bina taiyaari Interview ke lie gaya. (I went for the interview off hand.)",
            "main jevan ke utaar-chadhav se avagat hun. (I am familiar with the heads and tails of life.)", "hame aapa nahi khona chahiye. (We shouldn’t lose our temper.)",
            "vo aparadhi abhi bhi pakad se bahar hai. (That criminal is still at large.)", "ye badappan kibaat nahin. (It is not a matter of kindness)",
            "Mujhe to balki khushi hogi. (It will rather please me.)", "hamare paas bahut samay hai. (We have plenty of time.)", "Bas do minat mein main nikal gaya hota. (I would have left just in 2 mins.)",
            "yah kranti black money  ka pardafaash kar degi. (This revolution will unearth the black money.)", "main tumhare mamale mein nahi ghusna chahata. (I don’t want to delve into your matter.)",
            "mere kot ko pahan ke dekho. (Try my coat on.)", "Guests ki khatiradari karo. (Look after the guests.)", "Tumhara kam prashansani hai. (Your work is praiseworthy.)",
            "hamen apane aap ko chalak nahi samajhana chahie. (We should not think ourselves to be clever.)", "Main apani anumati deta hun. (I give my consent.)",
            "tumhe meri vajah se pareshani  jhelani padi. (You had to suffer because of me.)", "Is maamle ko kisi tarah sulajhao. (Settle this matter somehow.)",
            "meri taraph se pleej mafi mang lena. (Please apologize on my behalf.)", "rod durghatana mein kari log ghayal hue. (Many people got injured in the road mishap.)",
            "usne poori kitab padhi. (He went through the whole book.)", "main bina taiyaari Interview ke lie gaya. (I went for the interview off hand.)",
            "main jevan ke utaar-chadhav se avagat hun. (I am familiar with the heads and tails of life.)", "hame aapa nahi khona chahiye. (We shouldn’t lose our temper.)",
            "vo aparadhi abhi bhi pakad se bahar hai. (That criminal is still at large.)", "ye badappan kibaat nahin. (It is not a matter of kindness.)",
            "Mujhe to balki khushi hogi. (It will rather please me.)", "hamare paas bahut samay hai. (We have plenty of time.)", "Bas do minat mein main nikal gaya hota. (I would have left just in 2 mins.)",
            "yah kranti black money  ka pardafaash kar degi. (This revolution will unearth the black money.)", "main tumhare mamale mein nahi ghusna chahata. (I don’t want to delve into your matter.)",
            "mere kot ko pahan ke dekho. (Try my coat on.)", "Guests ki khatiradari karo. (Look after the guests.)", "Tumhara kam prashansani hai. (Your work is praiseworthy.)",
            "hamen apane aap ko chalak nahi samajhana chahie. (We should not think ourselves to be clever.)", "Main apani anumati deta hun. (I give my consent.)",
            "tumhe meri vajah se pareshani  jhelani padi. (You had to suffer because of me.)", "Is maamle ko kisi tarah sulajhao. (Settle this matter somehow.)",
            "meri taraph se pleej mafi mang lena. (Please apologize on my behalf.)", "rod durghatana mein kari log ghayal hue. (Many people got injured in the road mishap.)",
            "usne poori kitab padhi. (He went through the whole book.)", "vo der se uthata hai. (He is a late riser.)", "vo jaldi uthata hai.(He is an early riser.)",
            "Main Newspaper roz padhata hun. (I go through the newspaper every day.)", "aag bujha do kahin charon or na phail jae. (Put out the fire lest it should spread around.)",
            "usne IAS exam paas kar liya. (He got through the IAS exam.)", "vapis aate hue, main usake ghar gaya. (On the way back I went to his home.)",
            "main paudhon ko pani  de raha hun. (I am watering the plants.)", "Hamne raaste mein ek ajeeb cheez dekhi. (We saw an awkward thing on the way.)",
            "Tum mujhe mere jaise hi lagte ho. (You seem to me just like me.)", "Ek samay ki bat hai, ek raja tha. (Once upon a time, there was a king.)",
            "Main aaj vahan jaane vala hun. (I am supposed to go there today.)", "Main hamesha aapaka aabhari /shukragujar rahunga. (I will remain indebted to you.)",
            "Mujhe Doctor. samajhne ki galti mat karo. (Don’t mistake me for a Doctor.)", "Usne mujhe Engineer samajhne ki galati ki. (He mistook me for an Engineer.)",
            "Main tumse puri tarah sahmat nahi hu. (I don’t quite agree with you.)", "Ye kapde chhote ho gaye hain. (These clothes are worn out.)",
            "Yah jaanvar vilupt ho raha hai. (This animal is being extinct.)", "ye kamar tod dene vala kam hai. (This is a backpaining work.)",
            "mujhe thoda-thoda karake khilao. (Feed me bit by bit.)", "Hamne toss kiya, haid aaya. (We tossed the coin, it came down heads.)",
            "Hamne tos kiya, tels aaya. (We tossed the coin, it came down tails.)", "mere pas keval 3 chhuttiyan hain. (I have only 3 leaves in my credit.)",
            "vo meri salah ko nahi manata. (He doesn’t act on my advice.)", "Please thoda aur lejie. (Please have a little more.)", "main apki seva mein haazir hun. (I am at your service.)",
            "tumhen halke saman ke sath yatra karni chahie. (You should travel light.)", "bhagy ke samne koi nahi tik sakata. (Nobody can stand against fate.)",
            "Tum garibo par daya nahi karate. (You don’t show pity on the poor.)", "Aaj taptapati garmi hai. (It’s a blistering heat today.)",
            "Dhyan dejiye. (Please Pay attention)", "Maaf karen apako kasht hua. (Sorry to hurt you.)", "aaram se baithie. (Feel at home.)",
            "aap mujhe bolne do. (Let me speak.)", "apne ham par bahut krpa ki. (It’s very kind of you.)", "sochane ke lie vakt dejie. (Give me some time to think.)",
            "aapase milakar khushi hui. (Pleasure to meet you.)", "mujhe maut se dar nahin lagata. (I am not afraid of death.)",
            "tumhen sharm aani chahie. (You must be ashamed.)", "tumhari aisa karne ki himmat kaise hui ! (How dare you do so!)",
            "Aapke paas khone ko kuch nahi hai lekin paane ko bahut kuch. (You have nothing to lose but a lot to gain.)", "hamen iske parinamon ke liYe taiyar rahana chahie. (We should be ready for its consequences.)",
            "mujhe vo log pasand nahi jo igjaims se ek mahine pahale rat lete hain. (I don’t like those guys who cram at the last month before exams.)",
            "doktar ne use zinda rakhane ki  bahut koshish ki par koi phayada nahi hua. (Doctor tried a lot to keep him alive but of no avail/use.)",
            "sachin ka out hona bharat ke lie ek jabaradast jhataka hai. (Sachin’s departure is a terrible blow for India.)", "vo apane aap ko usase bat karane se nahi rok saki/payi. (She couldn’t hold herself talking to him.)",
            "vo padhane laga aur aisa dikhaya mano vo kaphi der se padh raha ho. (He started studying and made it look as if he had been studying for a long.)",
            "kalpana ki “ shadei ki  “shubh tithi 13 aprail 2013 hai. (The auspicious date of Kalpana’s marriage is 13th of April 2013.)",
            "Agar main apne uncle ko uncle na kahu to kya kahu? (If I don’t call uncle to my uncle, what do I say then?)", "Agar vo tumhe paise vaapas na de to? (What if he doesn’t return you the money?)",
            "Agar vo tumhe baat na kare to? (What if he doesn’t talk to you?)", "Mushkil se 10 rupay honge mere wallet me. (There would hardly be Rs. 10 in my wallet.)",
            "Main is saal shayad hi koi chhutti li hai. (I have hardly taken a leave this year.)", "Shiksha insan ko sabhya banaati hai. (Education makes one civilized.)",
            "Honi ko kaun taal sakta hai! (Who can change the destiny!)", "tumne kachare ko jalvaya hai (You have got the garbage burnt.)",
            "Kaash use mere dukh ka ahsas hota. (I wish he had the feel of my pain.)", "Aap me se kitne morning walk pr jaate hai? (How many of you go on a morning walk?)",
            "Paisa dena aasaan hai, lena bahut mushkil. (It’s easy to lend money but very difficult to take back.)", "Mere paas kal bhi paise nahi the, aaj bhi nahi hain. (I have never had money.)",
            "Aaj ke baad mujhe kabhi phone mat karna. (Don’t ever call me again.)", "Kya ho agar vo tumhara dil tod de to? (What if she breaks your heart?)",
            "Zindagi hame shaayad doosra mauka na de. (Maybe, life doesn’t give us another chance.)", "usne police ko soochit kar diya. (He reported to police)",
            "main is maamle me chashmdeed gawah hun (I am an eye witness in this case.)", "Aparaadhi ko bari kar diya gaya. (The criminal was acquitted.)",
            "Vo bhi kya din the! Kasam se! (How beautiful the days were! Really!)", "Aap bhi kya insan ho! Sach me! (How great a person you are! Really!)",
            "Kya ho agar vo tumhara dil tod de to? (What if he breaks your heart?)", "Agar vo aapki baat na maane to? (What if he doesn’t obey you?)",
            "Zindagi hame shaayad doosra mauka na de. (Maybe, life doesn’t give us another chance.)", "Vo mujhe shaayad doosra mauka na de. (Maybe, he doesn’t give me another chance.)",
            "Vo mujhe doosra mauka na de to? (What if he doesn’t give me another chance?)", "Kitna badal gaya yar tu. (How changed you are!)",
            "Samay kitna badal gaya hai. (How changed the time is!)", "Kaash tum mere hote. (I wish you were mine.)", "Kaash main tumhara hota. (I wish I were yours.)",
            "Pagal ho gaya hai kya tu? (Are you mad or what?)", "Ye building hai ya fir…? (Is it a building or what?)", "Ye pen hai ya fir…? (Is it a pen or what?)",
            "Jo ho raha hai, hone do. (Let happen, whatever is going on.)", "Yaade aaj bhi dhundli nahi hui hain. (Memories are not yet vanished.)",
            "Maine bahut koshish ki par sab bekaar. (I tried a lot but all in vain.)", "Maine bahut padhai ki par sab bekaar. (I studied a lot but all in vain.)",
            "Shayd hi kisi ne mujhe dhudne ki koshish ki. (I don’t think if someone tried to look for me.)", "Vo shayad hi kabhi mujhse mil paaye. (I don’t think if he could ever meet me.)",
            "Main koi kasar nahi chodunga. (I will leave no stone unturned.)", "Aapne chappale ulti pahan rakhi hain. (You have put on your slippers wrong sides.)",
            "Aapki chappale ulti padi hui hain. (Your slippers are lying upside down.)", "Kaash main ghar jaa pata! (I wish I could go home!)",
            "Kaash main aapse mil pata! (I wish I could meet you!)", "Main ye dil se karna chahta hu. (I want to do it wholeheartedly.)",
            "Main aapko dil se padhaana chahta hu. (I want to teach you wholeheartedly.)", "Aaj main kisi tarah bach gaya. (I somehow escaped today.)",
            "Mujhe ye Rs. 10 ka pada. (It cost me Rs. 10.)", "Hamare beech kuch nahi hai. (There is nothing between us.)", "Mene tumhara kya bigada hai? (What wrong have I done to you?)",
            "Usne tumhara kya bigada hai? (What wrong has he done to you?)", "Kisi ne tumhara kya bigada hai? (What wrong has anyone done to you?)",
            "Isme tumhara kya bigdega? (What wrong can it cause to you?)", "vo to tumhara sath dega hi. (He will obviously support you.)", "vo to vaha jayega hi. (He will obviously go there.)",
            "Usne hi to ye kaha tha. (It was he, who had said it.)", "Ram hi to yahaan aaya tha. (It was Ram, who had come here.)", "Vah fisal kar gir gaya. (He slipped and fell down.)",
            "Vah kuyein me fisal kar gir gaya. (He slipped and fell into the well.)", "Zubaan fisal gai. (It was a slip of tongue.)", "Tum mere aage nahi tik sakte. (You can’t stand against me.)",
            "Ye chappale zyada din tak nahi tikegi. (These slippers are not going to stay long.)", "us par chori ka ilzam hai (He is accused of theft.)",
            "us par chori karne ka ilzam hai (He is accused of stealing money.)", "ye aapki meharbani hai (It’s very kind of you.)", "aap vastav mein ek vinamra vyakti hain. (You are indeed a generous person.)",
            "apani ghadi thik karavao (Get your watch repaired.)", "vah kapade silai kar rahi hai. (She is stitching clothes.)", "ye log aapse milna chahte hain. (These people want to meet you.)",
            "uske sir mein dard hai (He has a headache.)", "Main aapka intzaar kar raha hun. (I am waiting for you.)", "Uth jaaiye, saat baj gaye hain. (Please get up, it’s 7 o’clock.)",
            "yah makaan kiraye ke liye khali hai (This house is to let.)", "ab rogi khatre se baahar hai. (Now the patient is out of danger.)",
            "Operation theatre kis or hai? (Which way is the operation theatre?)", "din ba din haalaat kharab ho rahe hai (Things are deteriorating day by day.)",
            "Mujhe apni sehat ki chinta hai. (I am worried about my health.)", "mera sir chakara raha hai (I am feeling giddy.)", "Aapko main kaise samjhaun? (How do I make you understand?)",
            "Shiksha vyavsaay nahi balki aap tak pahunchne ka ek prayas hai. (Education is not a business but an endeavor to reach you.)",
            "achchha hoga ki isi samay aap apna homework kar lo, varna… (Better you complete your homework right away or else…)",
            "achchha hoga ki aap isee samay T.V. band kar do. (Better you switch off the TV right away.)", "us aadami ne jo kiya tha use uske liye saja mili (The man got punished for what he had done.)",
            "tumhare pitaji mere pitaji se umr mein bade hain. (Your father is older than my father.)", "main janata hun ki ye samasya kis tarah hal ki ja sakti hai. (I know how this problem can be solved.)",
            "Jab vah khaana kha raha hota hai vah TV dekhata hai (He watches TV while having the food.)", "Jaha tak meri zindagi ka sawal hai, maine ise manav kalyaan par laga diya hai. (As far as my life is concerned, I have devoted it to human welfare.)",
            "Main tumhari surat nahin dekhana chahata. (I don’t want to even see your face.)", "kya ap mere lie thoda samay nikal sakte hain? (Can you please spare some time for me?)",
            "aisa galati se hua. (It happened by mistake.)", "aisa tukke se hua. (It happened by chance.)", "thoda khisakie. (Kindly move a bit.)",
            "kya main baith sakata hun? (May I sit?)", "meri or se manfi mang lijie. (Convey my apologies.)", "main puri koshish karunga. (I’ll try my level best.)",
            "ab main kuchh bolun. (May I say something now?)", "ummid hai aap maje mein ho. (Hope, you are enjoying yourself.)", "krpa karake dheere bolie. (Please speak slowly.)",
            "tum kya chahate ho? (What do you want?)", "tum kya soch rahe ho? (What are you thinking?)", "kya tum kuch bolna chahte ho? (Do you want to say something?)",
            "Isse kya fark padta hai? (What difference does it make?)", "Isse koi fark nahi padta. (It doesn’t make any difference.)", "Ram thoda let ho gaya. (Ram got a little late.)",
            "sunakar bahut dukh hua. (Sorry to hear that.)", "bachchon ka aadha ticket lagta hai (Children travel half-fare.)", "Use tala todna padega. (He will have to break the lock.)",
            "Mujhe iski yaad dila dena. (Please remind me of it.)", "Main aaj der se jaga. (I woke up a little late today.)", "tumhari himmat kaise huri ! (How dare you!)",
            "tum kha-makha preshan ho rahe ho. (You are stressed for no reason.)", "aaj khane mein naya kya hai? (What are the new dishes today?)",
            "apne se badon ka kahna maano (Obey your elders.)", "kisan ye beej nahin boyenge (The farmers will not sow these seeds.)",
            "main kabhi paise udhaar nahin leta (I never borrow money.)", "hum kharab maal kabhi nahin bechate (We never sell defective products.)",
            "kya tum kabhi jhuth nahin bolte? (Do you never tell a lie?)", "Hum hamesha samay par pahunchate hain (We always reach on time.)",
            "Usne mombatti bujhai. (He blew out the candle.)", "Meri daadi kahaniya suna rahi thi. (My grandmother was telling stories.)",
            "Kya tumne uski sikayat kar di? (Did you complain about him?)", "Meeting paanch ghante chali. (The meeting continued for five hours.)",
            "vo apane ansu nahi rok saki/payi. (She couldn’t hold weeping.)", "vo apani hansi nahi rok saki (She couldn’t control her laugh.)",
            "vah kya karega pakka nahin hai (What he will do is not decided.)", "Yah aapki duaaon ke karan hua. (It’s because of your good wishes.)",
            "aapka hamari baton se kya sambandh (How are you concerned with our affairs?)", "Tum bade chid-chide swabhav ke ho. (You are very short-tempered.)",
            "baat karake apana samay barbad mat karo (Don’t waste your time by talking.)", "aapako naukari se hataya jata hai (You are dismissed from the job.)",
            "Apne kaam se matlab rakho. (Mind your own business.)", "Kuchh bhi ho sakta tha. (Anything could have happened.)", "Kya tum car chalana jante ho? (Do you know how to drive the car?)",
            "Aisi chije main bhee nahi khata. (I don’t eat such things either.)", "Aisa khayal tak mujhe nahi aata. (I can’t even think so.)", "Tum mujhe rok nahi paoge. (You will not be able to stop me.)",
            "Uska bhi koi nahi tha. (Nobody was his either.)", "Na khud karte ho, na karne dete ho. (Neither you do yourself, nor let do.)",
            "Isi baat par to hamare beech jhagda hua. (That’s what we had a dispute on.)", "Koi to doshi hai, tum ya main? (Someone is to blame; either you or I?)", "Is tarah ka aadmi ghamandi hota hai. (Such a man is arrogant.)",
            "Wo abhi bhi bure dor se guzar raha hai. (He is still going through a bad phase.)", "Aesi kahaniya bahut boring hoti hai. (Such stories are extremely boring.)", "Maine vahi to us ajnabi ko dekha tha. (That’s where I had seen that stranger.)",
            "Isi tarah main bada hua. (That’s how I grew up.)", "Use bhi phal pasand nahi the. (He didn’t like fruits either.)",
            "Meri baat kisi ne nahi suni. (Nobody listened to me.)", "Use kya kehna hai? (What is he to say?)", "Mujhe tum yaad karte the. (You missed me.)",
            "Mujhe kuch kehna hai. (I have to say something.)", "Use chaplusi nahi karni chahiye. (He should not flatter.)", "Use dekhte hi mujhe khyal aaya ki maine use pahle bhi kahin dekha hai. (The moment I saw him, I thought that I had already seen him somewhere.)",
            "English seekhne ke liye sabse zaruri kya hai? (What matters the most to learn English?)", "Jaisa aap sochte hain, vaise hi ban jaate hain. (As you think, so you become.)",
            "Aap apne aap se puchiye. Kya aapne English ko vakayee apna 100% diya hai? (Ask yourself. Have you really given your 100% to English?)",
            "Jab aap fail hone ke baare me soche bina mehnat karte jaate hain, to akhirkaar safal ho hi jaate hain. (When you keep working hard without worrying about being failed, eventually you succeed.)",
            "Agar aap mehnat karenge to English zaroor bolenge",


    };


    Button getText_btn;
    ImageView showImage_img,reply_image;
    TextView showText_txt;
    static final int REQUEST_IMAGE_CAMERA = 1;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    String AES = "AES";

    String postid,adminId;

    String msg,trans_msg;

    FirebaseAuth mAuth;


    DatabaseReference UsersRef,RootRef;
    private TextView userLastSeen;

    private Uri fileUri;
    private ProgressDialog loadingBar;

    private List<String> fileNameList;
    private List<String> fileDoneList;

    ProgressBar splashProgress;

    private String imageurl;

    Dialog myDialog;

    CircleImageView profile_image;
    TextView username,messageConvert;

    private String cheker = "", myUrl = "", url = "";
    FirebaseUser fuser;
    DatabaseReference reference;
    private static final int RESULT_LOAD_IMAGE = 1;

    private StorageTask uploadTask;
    ImageButton btn_send, SendFilesButton,reply_send;
    EditText text_send,reply_edit,reply_password;


    GroupMessageAdapter messageAdapter;
    List<Comment> mchat;


    ArrayList<Comment> commentList = new ArrayList<>();

    RecyclerView recyclerView, findFriendLis,group_users_recyclerview;

    Intent intent;

    private String saveCurrentTime, saveCurrentDate;

    ValueEventListener seenListener;

    RelativeLayout reply_bottom,bottom,relative_layout_message;
    String messageId;;

    boolean notify = false;
    APIService apiService;


    AddFriendAdapter addFriendAdapter;


    TextView inputPassword,fab_status,reply_preview_message,reply_delete,type;
    Window window;
    String str_name;

    String imageReciever,bioReciever,fullnameReciever,imageSender,bioSender,fullnameSender,theLastMessage,outputString;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_massage_instagram);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        myDialog = new Dialog(this);
        if (Build.VERSION.SDK_INT >= 21) {
            window = this.getWindow();
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }


        notify = true;

        //This is additional feature, used to run a progress bar
        splashProgress = findViewById(R.id.splashProgress);


        //Method to run progress bar for 5 seconds


        apiService = Client.getClient("https://fcm.googleapis.com").create(APIService.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        group_users_recyclerview = (RecyclerView) findViewById(R.id.group_users_recyclerview);
        group_users_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        addFriendAdapter = new AddFriendAdapter(this, commentList);
        group_users_recyclerview.setAdapter(addFriendAdapter);


        splashProgress = findViewById(R.id.splashProgress);

        mAuth = FirebaseAuth.getInstance();

        //Method to run progress bar for 5 seconds

        bottom = findViewById(R.id.bottom);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        btn_send.setEnabled(false);
        inputPassword = findViewById(R.id.password);
        text_send = findViewById(R.id.text_send);
        reply_bottom = findViewById(R.id.reply_bottom);
        reply_bottom.setVisibility(View.GONE);
        reply_edit = findViewById(R.id.reply_edit);
        reply_image = findViewById(R.id.reply_image);
        reply_send = findViewById(R.id.reply_Send);
        type = findViewById(R.id.type);

        reply_preview_message = findViewById(R.id.preview_message);
        relative_layout_message = findViewById(R.id.relative_layout_message);
        reply_delete = findViewById(R.id.reply_delete);
        reply_password = findViewById(R.id.reply_password);
        reply_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative_layout_message.setVisibility(View.VISIBLE);
                reply_bottom.setVisibility(View.GONE);

            }
        });


        findFriendLis = findViewById(R.id.search_translate);



        LinearLayoutManager linearLayoutTranslate = new LinearLayoutManager(getApplicationContext());
        linearLayoutTranslate.setStackFromEnd(true);
        findFriendLis.setLayoutManager(linearLayoutTranslate);


        getText_btn = findViewById(R.id.btn_gettext);
        showText_txt = findViewById(R.id.txt_show_text);
        showImage_img = findViewById(R.id.img_imageview);
        messageConvert = findViewById(R.id.message_convert);
        //Set OnClick event for getImage_btn Button to take image from camera

        //Set OnClick event for getText_btn Button to get Text from image

        getText_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                GetTextFromImageFunction();
            }
        });

        fab_status = findViewById(R.id.fab_status);
        fab_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.smoothScrollToPosition(mchat.size());

                fab_status.setVisibility(View.GONE);
            }
        });




        loadingBar = new ProgressDialog(this);
        userLastSeen = (TextView) findViewById(R.id.custom_user_last_seen);
        RootRef = FirebaseDatabase.getInstance().getReference();

        intent = getIntent();
        postid = intent.getStringExtra("visit_user_id");
        adminId = intent.getStringExtra("adminId");




        fuser = FirebaseAuth.getInstance().getCurrentUser();

        UsersRef = FirebaseDatabase.getInstance().getReference().child(fuser.getUid());

        SendFilesButton = (ImageButton) findViewById(R.id.send_files_btn);
        SendFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtclose;
                LinearLayout image, images, video, videos, pdf, pdfs, ms_file, ms_files;

                Button btnFollow;
                myDialog.setContentView(R.layout.custompopup);
                txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
                image = (LinearLayout) myDialog.findViewById(R.id.image);
                images = (LinearLayout) myDialog.findViewById(R.id.images);
                video = (LinearLayout) myDialog.findViewById(R.id.video);
                videos = (LinearLayout) myDialog.findViewById(R.id.videos);
                pdf = (LinearLayout) myDialog.findViewById(R.id.pdf);
                pdfs = (LinearLayout) myDialog.findViewById(R.id.pdfs);
                ms_file = (LinearLayout) myDialog.findViewById(R.id.ms_word);
                ms_files = (LinearLayout) myDialog.findViewById(R.id.ms_words);
                txtclose.setText("M");
                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                    }
                });

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "image";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select Image"), 438);
                    }
                });

                images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "image";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(Intent.createChooser(intent, "Select Image"), RESULT_LOAD_IMAGE);

                    }
                });

                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "video";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("video/*");
                        startActivityForResult(intent.createChooser(intent, "Select Ms World File"), 438);

                    }
                });

                videos.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "video";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("video/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(intent.createChooser(intent, "Select Ms World File"), RESULT_LOAD_IMAGE);

                    }
                });

                pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "PDF";

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        startActivityForResult(intent.createChooser(intent, "Select PDF"), 438);


                    }
                });

                pdfs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();

                        cheker = "PDF";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/pdf");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(intent.createChooser(intent, "Select PDF"), RESULT_LOAD_IMAGE);

                    }
                });


                ms_file.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "docx";

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/msword");
                        startActivityForResult(intent.createChooser(intent, "Select Ms World File"), 438);

                    }
                });


                ms_files.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myDialog.dismiss();
                        cheker = "docx";

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("application/msword");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(intent.createChooser(intent, "Select Ms World File"), RESULT_LOAD_IMAGE);

                    }
                });


                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.show();
            }
        });



                toolbar = findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MassageActivityInstagram.this,GroupInfo.class);
                intent.putExtra("groupName",postid);
                intent.putExtra("adminId",adminId);
                startActivity(intent);
            }
        });



        text_send.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                   
                    btn_send.setEnabled(false);
                    findFriendLis.setVisibility(View.GONE);

                } else {
                    btn_send.setEnabled(true);
                    findFriendLis.setVisibility(View.VISIBLE);
                    typingStatus(fuser.getUid());
                    firebaseSearch(s.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        reply_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    typingStatus("noOne");
                    findFriendLis.setVisibility(View.GONE);

                } else {
                    findFriendLis.setVisibility(View.VISIBLE);
                    typingStatus(fuser.getUid());
                    firebaseSearch(s.toString().toLowerCase());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;

                try {
                    msg = encrypt(text_send.getText().toString(), inputPassword.getText().toString());
                    trans_msg = encrypt(messageConvert.getText().toString(), inputPassword.getText().toString());


                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (!msg.equals("")) {


                    addCommentSender(msg,trans_msg);

                } else {
                    Toast.makeText(MassageActivityInstagram.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
            }
        });




        reply_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = reply_edit.getText().toString();
                String ret_type = type.getText().toString();
                String ret_preview_message = reply_preview_message.getText().toString();
                if (!msg.equals("")){

                    try {

                        msg = encrypt(reply_edit.getText().toString(), reply_password.getText().toString());
                        trans_msg = encrypt(messageConvert.getText().toString(), reply_password.getText().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        outputString = decrypt(text_send.getText().toString(), inputPassword.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                     reply_edit.setText("");
                    relative_layout_message.setVisibility(View.VISIBLE);
                    reply_bottom.setVisibility(View.GONE);


                    replyCommentSender(msg,trans_msg,ret_type,ret_preview_message);



                } else {
                    Toast.makeText(MassageActivityInstagram.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }

            }
        });



        findFriendLis.setVisibility(View.GONE);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(adminId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("imageurl")) {
                        imageReciever = dataSnapshot.child("imageurl").getValue().toString();
                        bioReciever = dataSnapshot.child("bio").getValue().toString();
                        fullnameReciever = dataSnapshot.child("fullname").getValue().toString();




                        readMesagges();


                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        RerieveUserInfo();


        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(postid);
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("imageurl")) {
                        imageSender = dataSnapshot.child("imageurl").getValue().toString();
                        bioSender = dataSnapshot.child("bio").getValue().toString();
                        fullnameSender = dataSnapshot.child("fullname").getValue().toString();

                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(fuser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    str_name = dataSnapshot.child("fullname").getValue().toString();
                }
                else
                    Toast.makeText(MassageActivityInstagram.this, "", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void addCommentSender(String msg,String trans_msg) {


        RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);

        final String messagePushID = RootRef.push().getKey();


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
        saveCurrentDate = currendateFormat.format(calForDate.getTime());


        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currenTimeFormat.format(calForTime.getTime());



        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", saveCurrentTime);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("messageID", messagePushID);
        hashMap.put("sender", fuser.getUid());
        hashMap.put("receiver", postid);
        hashMap.put("message", msg);
        hashMap.put("bio", trans_msg);
        hashMap.put("type", "text");
        hashMap.put("isseen", false);
        hashMap.put("username",str_name);
        hashMap.put("lastSendMessage",msg);


        RootRef.child(messagePushID).setValue(hashMap);

        findFriendLis.setVisibility(View.GONE);
        text_send.setText("");
        messageConvert.setText("");


        readUser();


        // add user to chat fragment
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(postid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(postid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(postid)
                .child(fuser.getUid());
        chatRefReceiver.child("id").setValue(fuser.getUid());





        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
               }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    private void replyCommentSender(String message,String trans_msg,String rep_type,String preview_message) {


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
        saveCurrentDate = currendateFormat.format(calForDate.getTime());


        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);

        String commentid = reference.push().getKey();


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("time", saveCurrentTime);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("messageID", commentid);
        hashMap.put("sender", fuser.getUid());
        hashMap.put("receiver", postid);
        hashMap.put("isseen", false);
        hashMap.put("message", message);
        hashMap.put("bio", trans_msg);
        hashMap.put("type", rep_type);
        hashMap.put("lastSendMessage",message);
        hashMap.put("preMessage",preview_message);




        reference.child(commentid).setValue(hashMap);


        findFriendLis.setVisibility(View.GONE);


    }



    private void readMesagges() {
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment chat = snapshot.getValue(Comment.class);

                         mchat.add(chat);



                    messageAdapter = new GroupMessageAdapter(MassageActivityInstagram.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private void state(String online) {

        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid()).child("userState");


        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
        saveCurrentDate = currendateFormat.format(calForDate.getTime());


        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
        saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("state", online);
        hashMap.put("date", saveCurrentDate);
        hashMap.put("time", saveCurrentTime);

        reference.updateChildren(hashMap);
    }


    private void typingStatus(String typing) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("typingTo", typing);

        reference.updateChildren(hashMap);
    }

    private void RerieveUserInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(adminId);


        reference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if ((dataSnapshot.exists()) &&(dataSnapshot.hasChild(postid +"image") && (dataSnapshot.hasChild(postid +"status"))))
                        {
                            String retriveimage = dataSnapshot.child(postid +"image").getValue().toString();
                            String retrieveStatus = dataSnapshot.child(postid +"status").getValue().toString();


                            userLastSeen.setVisibility(View.VISIBLE);

                            userLastSeen.setText(retrieveStatus);




                            Picasso.get().load(retriveimage).into(profile_image);


                        }
                        else if((dataSnapshot.exists()) &&(dataSnapshot.hasChild(postid +"status"))){

                            String retrieveStatus = dataSnapshot.child(postid +"status").getValue().toString();


                            userLastSeen.setVisibility(View.VISIBLE);
                            userLastSeen.setText(retrieveStatus);




                        }
                        else if((dataSnapshot.exists()) &&(dataSnapshot.hasChild(postid +"image"))){

                            String retriveimage = dataSnapshot.child(postid +"image").getValue().toString();


                            Picasso.get().load(retriveimage).into(profile_image);


                        }
                        else {


                            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/instagramtest-fcbef.appspot.com/o/placeholder.png?alt=media&token=b09b809d-a5f8-499b-9563-5252262e9a49").into(profile_image);

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }



    private void readUser() {

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);

                }

                addFriendAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalItemsSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemsSelected; i++) {

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    String fileName = getFileName(fileUri);

                    if (!cheker.equals("image")) {

                        RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);

                        final String messagePushID = RootRef.push().getKey();
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");


                        final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");
                        uploadTask = filePath.putFile(fileUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {

                                if (!task.isSuccessful()) {
                                    throw task.getException();


                                }
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(MassageActivityInstagram.this, "upload successfully", Toast.LENGTH_SHORT).show();
                                    Uri downloadUrl = task.getResult();
                                    myUrl = downloadUrl.toString();

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                                    saveCurrentDate = currendateFormat.format(calForDate.getTime());


                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                                    HashMap<String, Object> groupMessageKey = new HashMap<>();
                                    RootRef.updateChildren(groupMessageKey);


                                    Map messageTextBody = new HashMap();

                                    try {
                                        msg = encrypt(cheker, inputPassword.getText().toString());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    messageTextBody.put("message", myUrl);
                                    messageTextBody.put("username", str_name);
                                    messageTextBody.put("name", fileUri.getLastPathSegment());
                                    messageTextBody.put("type", cheker);
                                    messageTextBody.put("sender", fuser.getUid());
                                    messageTextBody.put("receiver", postid);
                                    messageTextBody.put("messageID", messagePushID);
                                    messageTextBody.put("time", saveCurrentTime);
                                    messageTextBody.put("date", saveCurrentDate);
                                    messageTextBody.put("isseen", false);
                                    messageTextBody.put("lastSendMessage","ms0enhBo1KPFs/nSUHkvpg==\n");



                                    RootRef.child(messagePushID).updateChildren(messageTextBody);


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(MassageActivityInstagram.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (cheker.equals("image")) {
                        RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");



                        final String messagePushID = RootRef.push().getKey();

                        final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");

                        uploadTask = filePath.putFile(fileUri);
                        uploadTask.continueWithTask(new Continuation() {
                            @Override
                            public Object then(@NonNull Task task) throws Exception {

                                if (!task.isSuccessful()) {
                                    throw task.getException();


                                }
                                return filePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {

                                    Uri downloadUrl = task.getResult();
                                    myUrl = downloadUrl.toString();

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                                    saveCurrentDate = currendateFormat.format(calForDate.getTime());

                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                                    saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                                    try {
                                        msg = encrypt(cheker, inputPassword.getText().toString());


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Map messageTextBody = new HashMap();
                                    messageTextBody.put("message", myUrl);
                                    messageTextBody.put("username", str_name);
                                    messageTextBody.put("name", fileUri.getLastPathSegment());
                                    messageTextBody.put("type", cheker);
                                    messageTextBody.put("sender", fuser.getUid());
                                    messageTextBody.put("receiver", postid);
                                    messageTextBody.put("messageID", messagePushID);
                                    messageTextBody.put("time", saveCurrentTime);
                                    messageTextBody.put("date", saveCurrentDate);
                                    messageTextBody.put("lastSendMessage","kt5rg2/r78I081y/kXkhw==\n");




                                    RootRef.child(messagePushID).updateChildren(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(MassageActivityInstagram.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                                            } else {

                                                Toast.makeText(MassageActivityInstagram.this, "Error", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });

                                }
                            }
                        });
                    }

                }

                //Toast.makeText(MainActivity.this, "Selected Multiple Files", Toast.LENGTH_SHORT).show();

            } else

                Toast.makeText(this, "Please Select Multiple Items", Toast.LENGTH_SHORT).show();


        }


        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                }
                break;
        }

        if (requestCode == 438 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            fileUri = data.getData();


            if (!cheker.equals("image")) {


                RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");

                final String messagePushID = RootRef.push().getKey();

                final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");
                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();


                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(MassageActivityInstagram.this, "upload successfully", Toast.LENGTH_SHORT).show();
                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                            saveCurrentDate = currendateFormat.format(calForDate.getTime());


                            Calendar calForTime = Calendar.getInstance();
                            SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                            saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                            try {
                                msg = encrypt(cheker, inputPassword.getText().toString());


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            Map messageTextBody = new HashMap();


                            messageTextBody.put("message", myUrl);
                            messageTextBody.put("username", str_name);
                            messageTextBody.put("name", fileUri.getLastPathSegment());
                            messageTextBody.put("type", cheker);
                            messageTextBody.put("sender", fuser.getUid());
                            messageTextBody.put("receiver", postid);
                            messageTextBody.put("messageID", messagePushID);
                            messageTextBody.put("time", saveCurrentTime);
                            messageTextBody.put("date", saveCurrentDate);
                            messageTextBody.put("isseen", false);
                            messageTextBody.put("lastSendMessage","ms0enhBo1KPFs/nSUHkvpg==\n");




                            RootRef.child(messagePushID).updateChildren(messageTextBody);


                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(MassageActivityInstagram.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (cheker.equals("image")) {
                RootRef = FirebaseDatabase.getInstance().getReference("Comments").child(adminId).child(postid);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");




                final String messagePushID = RootRef.push().getKey();

                final StorageReference filePath = storageReference.child(messagePushID + "." + "jpg");

                uploadTask = filePath.putFile(fileUri);
                uploadTask.continueWithTask(new Continuation() {
                    @Override
                    public Object then(@NonNull Task task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();


                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {

                            Uri downloadUrl = task.getResult();
                            myUrl = downloadUrl.toString();

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currendateFormat = new SimpleDateFormat("MMM dd");
                            saveCurrentDate = currendateFormat.format(calForDate.getTime());

                            Calendar calForTime = Calendar.getInstance();
                            SimpleDateFormat currenTimeFormat = new SimpleDateFormat("hh:mm a");
                            saveCurrentTime = currenTimeFormat.format(calForTime.getTime());


                            try {
                                msg = encrypt(cheker, inputPassword.getText().toString());


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Map messageTextBody = new HashMap();
                            messageTextBody.put("message", myUrl);
                            messageTextBody.put("username", str_name);
                            messageTextBody.put("name", fileUri.getLastPathSegment());
                            messageTextBody.put("type", cheker);
                            messageTextBody.put("sender", fuser.getUid());
                            messageTextBody.put("receiver", postid);
                            messageTextBody.put("messageID", messagePushID);
                            messageTextBody.put("time", saveCurrentTime);
                            messageTextBody.put("date", saveCurrentDate);
                            messageTextBody.put("lastSendMessage","kt5rg2/r78I081y/kXkhw==\n");



                            RootRef.child(messagePushID).updateChildren(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(MassageActivityInstagram.this, "Message Sent Successfully...", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(MassageActivityInstagram.this, "Error", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });

                        }
                    }
                });
            } else {

                Toast.makeText(this, "nothing selected, Error.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    public void getSpeechInput(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 10);
        } else {
            Toast.makeText(this, "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    /*
    private void GetTextFromImageFunction() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(this, "Error occur", Toast.LENGTH_SHORT).show();
        } else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> textBlockSparseArray = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.valueAt(i);
                stringBuilder.append(textBlock.getValue());
                stringBuilder.append("\n");
            }
            //Show the text to TextView
            text_send.setText(stringBuilder.toString());
            //Thats All
        }

    }

     */


    private String encrypt(String Data, String password) throws Exception {
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptValue;

    }

    private SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AEs");
        return secretKeySpec;

    }

    private void playProgress() {
        ObjectAnimator.ofInt(splashProgress, "progress", 100)
                .setDuration(5000)
                .start();


    }

    @Override
    protected void onStart() {
        super.onStart();
        state("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        typingStatus("noOne");
        state("offline");
        findFriendLis.setVisibility(View.GONE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        typingStatus("noOne");
        state("offline");
        findFriendLis.setVisibility(View.GONE);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        typingStatus("noOne");
        findFriendLis.setVisibility(View.GONE);

    }



    class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView username, userStatus,textConvert;

        CircleImageView profileImage;


        public SearchViewHolder(@NonNull View itemView) {

            super(itemView);

            username = itemView.findViewById(R.id.transText);
            textConvert = itemView.findViewById(R.id.convet_text);


        }

    }



    private void firebaseSearch(String newText) {

        String query = newText.toLowerCase();

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference("TransSearch");

        Query ChatsRef = UsersRef.orderByChild("fullname").startAt(newText).endAt(newText + "\uf8ff");


        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(ChatsRef, User.class).build();


        final FirebaseRecyclerAdapter<User, SearchViewHolder> adapter =
                new FirebaseRecyclerAdapter<User, SearchViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SearchViewHolder holder, final int position, @NonNull final User model) {

                        holder.username.setText(model.getFullname());
                        holder.textConvert.setText(model.getBio());


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                text_send.setText(model.getFullname());
                                messageConvert.setText(model.getBio());

                                findFriendLis.setVisibility(View.GONE);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trans_search, viewGroup, false);
                        SearchViewHolder viewHolder = new SearchViewHolder(view);
                        return viewHolder;
                    }
                };
        findFriendLis.setAdapter(adapter);
        adapter.startListening();

    }

    private void lastMessage(final String userid) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats").child(fuser.getUid()).child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessageID();
                        }
                    }
                }

                switch (theLastMessage) {
                    case "default":
                        HashMap<String,Object> hashMap1 = new HashMap<>();

                        hashMap1.put("date",null);
                        break;

                    default:
                        username.setText(theLastMessage);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(fuser.getUid());
                        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference(userid);


                        HashMap<String,Object> hashMap = new HashMap<>();

                        hashMap.put("date",null);
                        reference.child(theLastMessage).setValue(hashMap);
                        reference1.child(theLastMessage).setValue(hashMap);



                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String decrypt(String outputString, String password) throws Exception{
        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodeValue = Base64.decode(outputString,Base64.DEFAULT);
        byte[] decvalue = c.doFinal(decodeValue);
        String decryptvalue = new String(decvalue);
        return decryptvalue;

    }




    public class GroupMessageAdapter extends RecyclerView.Adapter<GroupMessageAdapter.ViewHolder> {

        SimpleExoPlayer exoPlayer;

        private Context ctx;
        private FirebaseAuth mAuth;
        int SPLASH_TIME = 43200000; //This is 3 seconds
        private int current = 0;
        private int duration = 0;

        private SparseBooleanArray selected_items;
        private int current_selected_idx = -1;

        TextToSpeech textToSpeech;

        public static final int MSG_TYPE_LEFT = 0;
        public static final int MSG_TYPE_RIGHT = 1;
        public static final int MSG_TYPE_LEFT_IMAGE = 0;
        public static final int MSG_TYPE_RIGHT_IMAGE = 1;

        private Context mContext;
        private List<Comment> mChat;
        private String imageurl;
        String AES = "AES";
        String outputString, convertString,deleteUserId;

        FirebaseUser fuser;
        String userid;

        Uri imageUri;
        DatabaseReference rootref;

        public GroupMessageAdapter(List<Comment> mChat) {
            this.mChat = mChat;
        }

        public GroupMessageAdapter(Context mContext, List<Comment> mChat, String imageurl) {
            this.mChat = mChat;
            this.mContext = mContext;
            this.imageurl = imageurl;
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == MSG_TYPE_RIGHT) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.group_chat_item_right, parent, false);
                return new ViewHolder(view);
            } else {
                View view = LayoutInflater.from(mContext).inflate(R.layout.group_chat_item_left, parent, false);
                return new ViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            mAuth = FirebaseAuth.getInstance();
            final String messageSenderId = mAuth.getCurrentUser().getUid();
            final Comment chat = mChat.get(position);

            String fromMessageType = chat.getType();
            String fromUserID = chat.getSender();
            String messageId = chat.getMessageID();
            holder.show_message.setText(chat.getMessage());



            if (position == mChat.size() - 1) {
                if (chat.isIsseen()) {

                    holder.txt_seen.setText("Seen");

                }
                //Method to run progress bar for 5 seconds
                else {
                    holder.txt_seen.setText("Delivered");
                }
            } else {
                holder.txt_seen.setVisibility(View.GONE);
            }


            holder.pdfName.setVisibility(View.GONE);
            holder.textTimeDate.setVisibility(View.GONE);
            holder.imageTimeDate.setVisibility(View.GONE);
            holder.videoTimeDate.setVisibility(View.GONE);
            holder.linearpdf.setVisibility(View.GONE);
            holder.linearName.setVisibility(View.GONE);
            holder.show_message.setVisibility(View.GONE);
            holder.show_image.setVisibility(View.GONE);
            holder.linearimage.setVisibility(View.GONE);
            holder.linearpdf.setVisibility(View.GONE);
            holder.show_pdf.setVisibility(View.GONE);
            holder.linearText.setVisibility(View.GONE);
            holder.linearVideo.setVisibility(View.GONE);
            holder.delete_one_linear.setVisibility(View.GONE);
            holder.text_show_password.setVisibility(View.GONE);
            holder.password.setVisibility(View.GONE);
            holder.password_ok.setVisibility(View.GONE);
            holder.password_null.setVisibility(View.GONE);
            holder.pause.setVisibility(View.GONE);
            holder.play.setVisibility(View.GONE);
            holder.reply_image.setVisibility(View.GONE);
            holder.reply_pdf.setVisibility(View.GONE);
            holder.reply_video.setVisibility(View.GONE);
            holder.reply_text.setVisibility(View.GONE);

            holder.rep_text.setVisibility(View.GONE);
            holder.rep_image.setVisibility(View.GONE);
            holder.rep_video.setVisibility(View.GONE);
            holder.rep_pdf.setVisibility(View.GONE);

            holder.rep_chat_text.setVisibility(View.GONE);
            holder.rep_chat_image.setVisibility(View.GONE);
            holder.rep_chat_video.setVisibility(View.GONE);
            holder.rep_chat_pdf.setVisibility(View.GONE);

            holder.rep_message_text.setVisibility(View.GONE);
            holder.rep_message_image.setVisibility(View.GONE);
            holder.rep_message_video.setVisibility(View.GONE);
            holder.rep_message_pdf.setVisibility(View.GONE);


            /*
            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playvideo(position, holder);

                }
            });
            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pausevideo(position, holder);

                }
            });

            holder.linearVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.play.setVisibility(View.GONE);
                    holder.pause.setVisibility(View.VISIBLE);

                }
            });

             */




            if (fromMessageType.equals("text")) {
                if (fromUserID.equals(messageSenderId)) {
                    holder.linearText.setVisibility(View.VISIBLE);
                    holder.text_show_password.setVisibility(View.VISIBLE);
                    holder.reply_text.setVisibility(View.VISIBLE);

                    if (chat.getBio().equals("ol6lgGovgJomq4QGS6hThA==\n")) {

                        try {
                            outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        holder.text_show_password.setText(outputString);

                    }
                    else
                    {
                        try {
                            convertString = decrypt(chat.getBio(), holder.password_null.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        holder.text_show_password.setText(convertString);
                    }



                    holder.reply_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            try {
                                outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                            relative_layout_message.setVisibility(View.GONE);
                            reply_bottom.setVisibility(View.VISIBLE);

                            reply_preview_message.setText(outputString);

                            type.setText("reply_text");

                        }
                    });


                    holder.linearText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.linearText.setVisibility(View.VISIBLE);
                            holder.textTimeDate.setVisibility(View.VISIBLE);
                            holder.textTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());

                            try {
                                outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                            holder.show_message.setVisibility(View.VISIBLE);
                            holder.show_message.setText(outputString);
                            holder.password.setVisibility(View.VISIBLE);
                            holder.password_ok.setVisibility(View.VISIBLE);


                        }
                    });

                    holder.password_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                outputString = decrypt(chat.getMessage(), holder.password.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            holder.text_show_password.setText(outputString);



                            try {
                                convertString = decrypt(chat.getBio(), holder.password.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            holder.show_message.setVisibility(View.VISIBLE);
                            holder.show_message.setText(convertString);

                        }
                    });


                    holder.linearText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });


                } else {


                    holder.linearText.setVisibility(View.VISIBLE);
                    holder.text_show_password.setVisibility(View.VISIBLE);
                    holder.textUserName.setText(chat.getUsername());
                    holder.reply_text.setVisibility(View.VISIBLE);


                    if (chat.getBio().equals("ol6lgGovgJomq4QGS6hThA==\n")) {

                        try {
                            outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                        holder.text_show_password.setText(outputString);

                    }
                    else
                    {
                        try {
                            convertString = decrypt(chat.getBio(), holder.password_null.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        holder.text_show_password.setText(convertString);
                    }

                    holder.reply_text.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            try {
                                outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                            relative_layout_message.setVisibility(View.GONE);
                            reply_bottom.setVisibility(View.VISIBLE);

                            reply_preview_message.setText(outputString);

                            type.setText("reply_text");

                        }
                    });


                    holder.linearText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.linearText.setVisibility(View.VISIBLE);
                            holder.textTimeDate.setVisibility(View.VISIBLE);
                            holder.textTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());


                            try {
                                convertString = decrypt(chat.getBio(), holder.password_null.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                            holder.show_message.setVisibility(View.VISIBLE);
                            holder.show_message.setText(convertString);
                            holder.password.setVisibility(View.VISIBLE);
                            holder.password_ok.setVisibility(View.VISIBLE);


                        }
                    });

                    holder.password_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                outputString = decrypt(chat.getMessage(), holder.password.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            holder.text_show_password.setText(outputString);


                            try {
                                convertString = decrypt(chat.getBio(), holder.password.getText().toString());
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                            holder.show_message.setVisibility(View.VISIBLE);
                            holder.show_message.setText(convertString);
                        }
                    });

                    holder.linearText.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });



                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });
                }

            } else if (fromMessageType.equals("image")) {

                if (fromUserID.equals(messageSenderId)) {
                    holder.show_image.setVisibility(View.VISIBLE);
                    holder.linearimage.setVisibility(View.VISIBLE);
                    holder.imageTimeDate.setVisibility(View.VISIBLE);
                    holder.imageTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                    Picasso.get().load(chat.getMessage()).into(holder.show_image);


                    holder.reply_image.setVisibility(View.VISIBLE);
                    holder.reply_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            relative_layout_message.setVisibility(View.GONE);
                            reply_bottom.setVisibility(View.VISIBLE);

                            reply_preview_message.setText(chat.getMessage());

                            type.setText("reply_image");

                            Picasso.get().load(chat.getMessage()).into(reply_image);

                        }
                    });





                    holder.show_image.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {
                            Intent chatIntent = new Intent(holder.itemView.getContext(), ImageViewActivity.class);
                            chatIntent.putExtra("url", mChat.get(position).getMessage());


                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair<View, String>(holder.show_image, "imageTransition");

                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(), pairs);


                            holder.itemView.getContext().startActivity(chatIntent, options.toBundle());
                        }
                    });


                    holder.show_image.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });


                    holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mChat.get(position).getMessage()));
                            holder.itemView.getContext().startActivity(intent);
                        }
                    });


                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });


                } else {

                    holder.show_image.setVisibility(View.VISIBLE);
                    holder.linearimage.setVisibility(View.VISIBLE);
                    holder.imageTimeDate.setVisibility(View.VISIBLE);
                    holder.imageUserName.setText(chat.getUsername());
                    holder.imageTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                    Picasso.get().load(chat.getMessage()).into(holder.show_image);


                    holder.reply_image.setVisibility(View.VISIBLE);
                    holder.reply_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            relative_layout_message.setVisibility(View.GONE);
                            reply_bottom.setVisibility(View.VISIBLE);

                            reply_preview_message.setText(chat.getMessage());

                            type.setText("reply_image");

                            Picasso.get().load(chat.getMessage()).into(reply_image);

                        }
                    });


                    holder.show_image.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onClick(View v) {
                            Intent chatIntent = new Intent(holder.itemView.getContext(), ImageViewActivity.class);
                            chatIntent.putExtra("url", mChat.get(position).getMessage());


                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair<View, String>(holder.show_image, "imageTransition");

                            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) holder.itemView.getContext(), pairs);


                            holder.itemView.getContext().startActivity(chatIntent, options.toBundle());
                        }
                    });


                    holder.show_image.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);

                            return true;
                        }
                    });


                    holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mChat.get(position).getMessage()));
                            holder.itemView.getContext().startActivity(intent);
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });

                }
            } else if (fromMessageType.equals("video")) {
                if (fromUserID.equals(messageSenderId)) {

                    holder.linearVideo.setVisibility(View.VISIBLE);
                    holder.videoTimeDate.setVisibility(View.VISIBLE);
                    holder.videoTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                    holder.show_video.setVisibility(View.VISIBLE);
                    holder.reply_video.setVisibility(View.VISIBLE);
/*
                        holder.show_video.setVideoPath(chat.getMessage());

                        holder.show_video.requestFocus();
                        holder.show_video.seekTo(1);
                        holder.play.setVisibility(View.VISIBLE);

 */


                    try {
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(holder.itemView.getContext()).build();
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(holder.itemView.getContext());
                        Uri video = Uri.parse(mChat.get(position).getMessage());
                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
                        holder.show_video.setPlayer(exoPlayer);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(false);

                    } catch (Exception e) {
                        Log.e("ViewHolder", "exoplayer error" + e.toString());

                    }


                    holder.reply_video.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            relative_layout_message.setVisibility(View.GONE);
                            reply_bottom.setVisibility(View.VISIBLE);

                            reply_preview_message.setText(mChat.get(position).getMessage());

                            type.setText("reply_video");

                            Picasso.get().load(chat.getMessage()).into(reply_image);

                        }
                    });


                    holder.open_video_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(holder.itemView.getContext(), DirectVideo.class);
                            intent.putExtra("visit_user_id", chat.getReceiver());
                            intent.putExtra("messageId", mChat.get(position).getMessage());
                            holder.itemView.getContext().startActivity(intent);

                        }
                    });



                    holder.linearVideo.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });


                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });




                } else {

                    holder.linearVideo.setVisibility(View.VISIBLE);
                    holder.videoTimeDate.setVisibility(View.VISIBLE);
                    holder.videoTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                    holder.show_video.setVisibility(View.VISIBLE);
                    holder.videoUserName.setText(chat.getUsername());

                        /*

                        holder.show_video.setVideoPath(chat.getMessage());

                        holder.show_video.requestFocus();
                        holder.show_video.seekTo(1);

                        holder.play.setVisibility(View.VISIBLE);

                         */


                    try {
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(holder.itemView.getContext()).build();
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(holder.itemView.getContext());
                        Uri video = Uri.parse(mChat.get(position).getMessage());
                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
                        holder.show_video.setPlayer(exoPlayer);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(false);

                    } catch (Exception e) {
                        Log.e("ViewHolder", "exoplayer error" + e.toString());

                    }


                    holder.reply_video.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            relative_layout_message.setVisibility(View.GONE);
                            reply_bottom.setVisibility(View.VISIBLE);

                            reply_preview_message.setText(mChat.get(position).getMessage());

                            type.setText("reply_video");

                            Picasso.get().load(chat.getMessage()).into(reply_image);

                        }
                    });


                    holder.open_video_activity.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Intent intent = new Intent(holder.itemView.getContext(), DirectVideo.class);
                            intent.putExtra("visit_user_id", chat.getReceiver());
                            intent.putExtra("messageId", mChat.get(position).getMessage());
                            holder.itemView.getContext().startActivity(intent);

                        }
                    });

                    holder.show_video.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });


                }
            } else if (fromMessageType.equals("PDF") || (fromMessageType.equals("docx"))) {
                {
                    if (fromUserID.equals(messageSenderId)) {
                        holder.linearpdf.setVisibility(View.VISIBLE);
                        holder.pdfName.setVisibility(View.VISIBLE);
                        holder.pdfName.setText(chat.getName());
                        holder.linearName.setVisibility(View.VISIBLE);
                        holder.show_pdf.setVisibility(View.VISIBLE);
                        holder.pdfTimeDate.setVisibility(View.VISIBLE);
                        holder.pdfTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6")
                                .into(holder.show_pdf);


                        holder.reply_pdf.setVisibility(View.VISIBLE);
                        holder.reply_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                relative_layout_message.setVisibility(View.GONE);
                                reply_bottom.setVisibility(View.VISIBLE);
                                reply_preview_message.setText(chat.getName() + ".pdf");
                                type.setText("reply_pdf");


                                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6")
                                        .into(reply_image);


                            }
                        });


                        holder.show_pdf.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                holder.delete_one_linear.setVisibility(View.VISIBLE);
                                holder.delete_one_message.setVisibility(View.VISIBLE);
                                holder.download_pdf.setVisibility(View.VISIBLE);

                                return true;
                            }
                        });


                        holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mChat.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });


                        holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteMessageForEveryone(position, holder);
                            }
                        });



                    } else {
                        holder.linearName.setVisibility(View.VISIBLE);
                        holder.pdfName.setVisibility(View.VISIBLE);
                        holder.pdfName.setText(chat.getName());
                        holder.linearpdf.setVisibility(View.VISIBLE);
                        holder.pdfTimeDate.setVisibility(View.VISIBLE);
                        holder.pdfUserName.setText(chat.getUsername());
                        holder.pdfTimeDate.setText(chat.getDate() + "  -  " + chat.getTime());
                        holder.show_pdf.setVisibility(View.VISIBLE);
                        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6")
                                .into(holder.show_pdf);


                        holder.reply_pdf.setVisibility(View.VISIBLE);
                        holder.reply_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                relative_layout_message.setVisibility(View.GONE);
                                reply_bottom.setVisibility(View.VISIBLE);

                                reply_preview_message.setText(chat.getName() + ".Pdf");

                                type.setText("reply_pdf");

                                Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6")
                                        .into(reply_image);


                            }
                        });


                        holder.show_pdf.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                holder.delete_one_linear.setVisibility(View.VISIBLE);
                                holder.delete_one_message.setVisibility(View.VISIBLE);
                                holder.download_pdf.setVisibility(View.VISIBLE);
                                return true;
                            }
                        });







                        holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mChat.get(position).getMessage()));
                                holder.itemView.getContext().startActivity(intent);
                            }
                        });


                        holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteSentMessage(position, holder);
                            }
                        });


                    }
                }
            } else if (fromMessageType.equals("reply_text")) {
                if (fromUserID.equals(messageSenderId)) {

                    holder.rep_message_text.setVisibility(View.VISIBLE);
                    holder.rep_chat_text.setVisibility(View.VISIBLE);
                    holder.rep_text.setVisibility(View.VISIBLE);


                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    holder.rep_message_text.setText(chat.getPreMessage());
                    holder.rep_chat_text.setText(outputString);



                    holder.rep_text.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });



                } else {
                    holder.rep_message_text.setVisibility(View.VISIBLE);
                    holder.rep_chat_text.setVisibility(View.VISIBLE);
                    holder.rep_text.setVisibility(View.VISIBLE);

                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    holder.rep_message_text.setText(chat.getPreMessage());
                    holder.rep_chat_text.setText(outputString);


                    holder.rep_text.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });


                }
            } else if (fromMessageType.equals("reply_image")) {
                if (fromUserID.equals(messageSenderId)) {

                    holder.rep_message_image.setVisibility(View.VISIBLE);
                    holder.rep_chat_image.setVisibility(View.VISIBLE);
                    holder.rep_image.setVisibility(View.VISIBLE);


                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    Picasso.get().load(chat.getPreMessage()).into(holder.rep_message_image);
                    holder.rep_chat_image.setText(outputString);



                    holder.rep_message_image.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });


                } else {
                    holder.rep_message_image.setVisibility(View.VISIBLE);
                    holder.rep_chat_image.setVisibility(View.VISIBLE);
                    holder.rep_image.setVisibility(View.VISIBLE);


                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    Picasso.get().load(chat.getPreMessage()).into(holder.rep_message_image);
                    holder.rep_chat_image.setText(outputString);



                    holder.rep_message_image.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });


                }

            } else if (fromMessageType.equals("reply_video")) {
                if (fromUserID.equals(messageSenderId)) {
                    holder.rep_message_video.setVisibility(View.VISIBLE);
                    holder.rep_chat_video.setVisibility(View.VISIBLE);
                    holder.rep_video.setVisibility(View.VISIBLE);

                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    holder.rep_chat_video.setText(outputString);

                    try {
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(holder.itemView.getContext()).build();
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(holder.itemView.getContext());
                        Uri video = Uri.parse(mChat.get(position).getPreMessage());
                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
                        holder.rep_message_video.setPlayer(exoPlayer);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(false);


                    } catch (Exception e) {
                        Log.e("ViewHolder", "exoplayer error" + e.toString());

                    }


                    holder.rep_video.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });

                } else {
                    holder.rep_message_video.setVisibility(View.VISIBLE);
                    holder.rep_chat_video.setVisibility(View.VISIBLE);
                    holder.rep_video.setVisibility(View.VISIBLE);

                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    holder.rep_chat_video.setText(outputString);

                    try {
                        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter.Builder(holder.itemView.getContext()).build();
                        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                        exoPlayer = (SimpleExoPlayer) ExoPlayerFactory.newSimpleInstance(holder.itemView.getContext());
                        Uri video = Uri.parse(mChat.get(position).getPreMessage());
                        DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("video");
                        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                        MediaSource mediaSource = new ExtractorMediaSource(video, dataSourceFactory, extractorsFactory, null, null);
                        holder.rep_message_video.setPlayer(exoPlayer);
                        exoPlayer.prepare(mediaSource);
                        exoPlayer.setPlayWhenReady(false);


                    } catch (Exception e) {
                        Log.e("ViewHolder", "exoplayer error" + e.toString());

                    }


                    holder.rep_video.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });
                }

            } else if (fromMessageType.equals("reply_pdf")) {
                if (fromUserID.equals(messageSenderId)) {
                    holder.rep_message_pdf.setVisibility(View.VISIBLE);
                    holder.rep_chat_pdf.setVisibility(View.VISIBLE);
                    holder.rep_pdf.setVisibility(View.VISIBLE);
                    holder.rep_pdf_name.setVisibility(View.VISIBLE);
                    holder.rep_pdf_name.setText(chat.getPreMessage());

                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6")
                            .into(holder.rep_message_pdf);


                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }



                    holder.rep_chat_pdf.setText(outputString);


                    holder.rep_pdf.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });


                    holder.download_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mChat.get(position).getPreMessage()));
                            holder.itemView.getContext().startActivity(intent);
                        }
                    });


                } else {
                    holder.rep_message_pdf.setVisibility(View.VISIBLE);
                    holder.rep_chat_pdf.setVisibility(View.VISIBLE);
                    holder.rep_pdf.setVisibility(View.VISIBLE);
                    holder.rep_pdf_name.setVisibility(View.VISIBLE);
                    holder.rep_pdf_name.setText(chat.getPreMessage());


                    Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/watsapp-6d8e6.appspot.com/o/file.png?alt=media&token=da730f2c-5760-4d67-b45b-b8ff497039b6")
                            .into(holder.rep_message_pdf);


                    try {
                        outputString = decrypt(chat.getMessage(), holder.password_null.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    holder.rep_chat_pdf.setText(outputString);



                    holder.rep_pdf.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            holder.delete_one_linear.setVisibility(View.VISIBLE);
                            holder.delete_one_message.setVisibility(View.VISIBLE);
                            holder.download_pdf.setVisibility(View.GONE);
                            return true;
                        }
                    });

                    holder.delete_one_message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteSentMessage(position, holder);
                        }
                    });

                }


            } else {
          //      holder.fullLinesrvideo.setVisibility(View.GONE);

            }





        }


        @Override
        public int getItemCount() {
            return mChat.size();
        }



        public  class ViewHolder extends RecyclerView.ViewHolder {

            public TextView text_show_password,play,pause,show_message,imageTimeDate,videoTimeDate,pdfTimeDate,delete_one_message,download_pdf
                    ,open_video_activity,reply_image,reply_pdf,reply_video,reply_text,rep_message_text
                    ,rep_chat_text,rep_chat_image,rep_chat_video,rep_chat_pdf,rep_pdf_name;
            public ImageView profile_image,show_image,show_pdf,rep_message_image,rep_message_pdf;
            VideoView full_video;
            EditText  password,password_null;
            public TextView txt_seen,textTimeDate,pdfName,full_screen,iconHidden,textUserName,imageUserName,videoUserName
                    ,pdfUserName;
            LinearLayout linearText,delete_text_linear,linearName;
            RelativeLayout  linearimage,linearVideo,linearpdf,rep_text,rep_image,rep_video,rep_pdf,delete_one_linear;


            private PlayerView show_video,rep_message_video;

            Button password_ok;
            ProgressBar splashProgress;

            public ViewHolder(View itemView) {
                super(itemView);


                password = itemView.findViewById(R.id.password);
                password_null = itemView.findViewById(R.id.password_null);
                password_ok = itemView.findViewById(R.id.password_ok);
                text_show_password = itemView.findViewById(R.id.show_message_password);
                splashProgress = itemView.findViewById(R.id.splashProgress);
                imageTimeDate = itemView.findViewById(R.id.imageTimeDate);
                show_message = itemView.findViewById(R.id.show_message);
                linearText = itemView.findViewById(R.id.lineartext);
                profile_image = itemView.findViewById(R.id.profile_image);
                txt_seen = itemView.findViewById(R.id.txt_seen);
                show_image = itemView.findViewById(R.id.show_image_image);
                textTimeDate = itemView.findViewById(R.id.texttimeDate);
                linearimage = itemView.findViewById(R.id.linearsuri);
                linearpdf = itemView.findViewById(R.id.linearsuripdf);
                show_pdf = itemView.findViewById(R.id.show_pdf);
                show_video = itemView.findViewById(R.id.show_Video);
                linearVideo = itemView.findViewById(R.id.linearsuriVideo);
                videoTimeDate = itemView.findViewById(R.id.videoTimeDate);
                delete_one_linear = itemView.findViewById(R.id.delete_one_linear);
                delete_one_message = itemView.findViewById(R.id.delete_one_message);
                download_pdf = itemView.findViewById(R.id.download_pdf);
                delete_text_linear =itemView.findViewById(R.id.delete_text_linear);
                pdfName = itemView.findViewById(R.id.pdfName);
                linearName = itemView.findViewById(R.id.linearName);
                pdfTimeDate = itemView.findViewById(R.id.pdfTimeDate);
                play =itemView.findViewById(R.id.play);
                pause = itemView.findViewById(R.id.pause);
                textUserName = itemView.findViewById(R.id.userNameText);
                pdfUserName = itemView.findViewById(R.id.userNamePdf);
                imageUserName = itemView.findViewById(R.id.userNameImage);
                videoUserName = itemView.findViewById(R.id.userNameVideo);

                open_video_activity = itemView.findViewById(R.id.open_video_activity);

                reply_image = itemView.findViewById(R.id.replyImage);
                reply_pdf = itemView.findViewById(R.id.replyPdf);
                reply_video = itemView.findViewById(R.id.replyVideo);
                reply_text = itemView.findViewById(R.id.replyText);



                rep_text = itemView.findViewById(R.id.rep_text);
                rep_image = itemView.findViewById(R.id.rep_image);
                rep_video = itemView.findViewById(R.id.rep_video);
                rep_pdf = itemView.findViewById(R.id.rep_pdf);


                rep_message_text = itemView.findViewById(R.id.rep_message_text);
                rep_message_image = itemView.findViewById(R.id.rep_message_image);
                rep_message_video = itemView.findViewById(R.id.rep_message_video);
                rep_message_pdf = itemView.findViewById(R.id.rep_message_pdf);

                rep_chat_text = itemView.findViewById(R.id.rep_chat_text);
                rep_chat_image = itemView.findViewById(R.id.rep_chat_image);
                rep_chat_video = itemView.findViewById(R.id.rep_chat_video);
                rep_chat_pdf = itemView.findViewById(R.id.rep_chat_pdf);



                rep_pdf_name = itemView.findViewById(R.id.rep_pdf_name);



            }
        }

        @Override
        public int getItemViewType(int position) {
            fuser = FirebaseAuth.getInstance().getCurrentUser();
            if (mChat.get(position).getSender().equals(fuser.getUid())) {
                return MSG_TYPE_RIGHT;
            } else  {
                return MSG_TYPE_LEFT;
            }

        }


        private void deleteSentMessage(int position, final ViewHolder holder) {

            String suri = FirebaseDatabase.getInstance().getReference().child(fuser.getUid()).child(mChat.get(position).getReceiver()).getKey();
            Comment chat = mChat.get(position);

            String messageId = mChat.get(position).getMessageID();

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();


            rootRef.child("Comments")
                    .child(adminId)
                    .child(mChat.get(position).getReceiver())
                    .child(mChat.get(position).getMessageID())
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {



                        Toast.makeText(holder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Error occurred..", Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
        private void deleteReceiveMessage(int position, final ViewHolder holder) {

            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.child("Comments")
                    .child(adminId)
                    .child(mChat.get(position).getReceiver())
                    .child(mChat.get(position).getMessageID())
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(holder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(holder.itemView.getContext(), "Error occurred..", Toast.LENGTH_SHORT).show();

                    }
                }
            });

        }

        private void deleteMessageForEveryone(final int position, final ViewHolder holder) {

            Comment chat = mChat.get(position);
            String messageId = mChat.get(position).getMessageID();

            final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            rootRef.child("Comments")
                    .child(adminId)
                    .child(mChat.get(position).getReceiver())
                    .child(mChat.get(position).getMessageID())
                    .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        holder.itemView.setVisibility(View.INVISIBLE);

                        rootRef.child("Comments")
                                .child(mChat.get(position).getReceiver())
                                .child(adminId)
                                .child(mChat.get(position).getMessageID())
                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){

                                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully...", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });                }
                    else
                    {
                        Toast.makeText(holder.itemView.getContext(), "Error occurred..", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        private void playProgress(final int position, final ViewHolder holder) {
            ObjectAnimator.ofInt(holder.splashProgress, "progress", 100)
                    .setDuration(5000)
                    .start();

        }


        private String decrypt(String outputString, String password) throws Exception{
            SecretKeySpec key = generateKey(password);
            Cipher c = Cipher.getInstance(AES);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeValue = Base64.decode(outputString,Base64.DEFAULT);
            byte[] decvalue = c.doFinal(decodeValue);
            String decryptvalue = new String(decvalue);
            return decryptvalue;

        }

        private String encrypt(String Data, String password) throws Exception {
            SecretKeySpec key = generateKey(password);
            Cipher c = Cipher.getInstance(AES);
            c.init(Cipher.ENCRYPT_MODE,key);
            byte[] encVal = c.doFinal(Data.getBytes());
            String encryptValue = Base64.encodeToString(encVal,Base64.DEFAULT);
            return encryptValue;

        }

        private SecretKeySpec generateKey(String password) throws Exception{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = password.getBytes("UTF-8");
            digest.update(bytes,0,bytes.length);
            byte[] key = digest.digest();
            SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AEs");
            return secretKeySpec;

        }

        private void playvideo(int position, final ViewHolder holder) {
            //     holder.show_video.start();
            holder.play.setVisibility(View.GONE);
        }

        private void pausevideo(int position, final ViewHolder holder) {
            //         holder.show_video.pause();
            holder.pause.setVisibility(View.GONE);
            holder.play.setVisibility(View.VISIBLE);
        }


    }






    public class AddFriendAdapter extends RecyclerView.Adapter<AddFriendAdapter.ContactViewHolder> {

        List<Comment> mChats;
        Context context;
        String messageId;

        String group;

        boolean isClick = false;

        public AddFriendAdapter(Context context, List<Comment> mChats) {
            this.mChats = mChats;
            this.context = context;
        }

        @NonNull
        @Override
        public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_chat_fragment, parent, false);
            ContactViewHolder viewHolder = new ContactViewHolder(view);
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

            Comment model = mChats.get(position);



                sendNotifiaction(model.getReceiver(), str_name, msg);

        }


        @Override
        public int getItemCount() {
            return mChats.size();
        }

        public class ContactViewHolder extends RecyclerView.ViewHolder {

            TextView username, userStatus;
            ImageView checkTextAddPeople;

            CircleImageView profileImage;

            TextView checkButtonTrue, checkButtonfalse;

            public ContactViewHolder(@NonNull View itemView) {

                super(itemView);

                username = itemView.findViewById(R.id.user_profile_name);
                userStatus = itemView.findViewById(R.id.user_status);
                profileImage = itemView.findViewById(R.id.users_profile_image);
                checkButtonTrue = itemView.findViewById(R.id.checkAddPeopleTrue);
                checkButtonfalse = itemView.findViewById(R.id.checkAddPeopleFalse);
                checkTextAddPeople = itemView.findViewById(R.id.checkTextAddPeople);


            }
        }
    }


    private void sendNotifiaction(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, username+": "+message, "New Message",
                            receiver);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(MassageActivityInstagram.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}