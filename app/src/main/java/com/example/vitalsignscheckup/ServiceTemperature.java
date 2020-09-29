package com.example.vitalsignscheckup;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ServiceTemperature extends Service {

    private static final String TAG = "ServiceTemperature"; // para debug

    private Runnable runnable;
    private BroadcastReceiver br;

    int temp;

    int count = 0;
    int sample_rate;
    int value_i;
    int value_rate;

    private IBinder mBinder = new MyBinder();
    private Handler mHandler;
    private Boolean isPaused;

    int DATA_SIZE = 1000;
    boolean COLLECT_DATA = true;


    ArrayList<Double> data = new ArrayList<Double>(Arrays.<Double>asList(
            34352.0, 34059.0, 33860.0, 33752.0, 33888.0, 34028.0, 34108.0, 34184.0, 34130.0, 34008.0, 33856.0, 33672.0, 33424.0, 33296.0, 33106.0, 32968.0, 32800.0, 32688.0, 32584.0, 32540.0, 32434.0, 32416.0, 32380.0, 32356.0, 32288.0, 32225.0, 32185.0, 32096.0, 32032.0, 31968.0, 31936.0, 32185.0, 33000.0, 34120.0, 35124.0, 35856.0, 36260.0, 36338.0, 36320.0, 36210.0, 36068.0, 35996.0, 35828.0, 35640.0, 35364.0, 35122.0, 34864.0, 34890.0, 34960.0, 35000.0, 35008.0, 34944.0, 34844.0, 34708.0, 34540.0, 34376.0, 34228.0, 34060.0, 33893.0, 33744.0, 33600.0, 33492.0, 33376.0, 33300.0, 33200.0, 33144.0, 33088.0, 32976.0, 32920.0, 32896.0, 32840.0, 32858.0, 33196.0, 33936.0, 35008.0, 35828.0, 36362.0, 36640.0, 36616.0, 36712.0, 36544.0, 36472.0, 36416.0, 36232.0, 35930.0, 35632.0, 35348.0, 35210.0, 35192.0, 35196.0, 35192.0, 35140.0, 35080.0, 34824.0, 34760.0, 34504.0, 34368.0, 34178.0, 33984.0, 33792.0, 33548.0, 33467.0, 33361.0, 33272.0, 33185.0, 33056.0, 32972.0, 32880.0, 32796.0, 32720.0, 32608.0, 32440.0, 32360.0, 32306.0, 32192.0, 32065.0, 31949.0, 31856.0, 31745.0, 31624.0, 31552.0, 31674.0, 32352.0, 33496.0, 34676.0, 35504.0, 35940.0, 36056.0, 36008.0, 35864.0, 35656.0, 35490.0, 35378.0, 35120.0, 34837.0, 34472.0, 34168.0, 34016.0, 33976.0, 34008.0, 33952.0, 33896.0, 33768.0, 33576.0, 33472.0, 33300.0, 33024.0, 32704.0, 32516.0, 32404.0, 32264.0, 32192.0, 32112.0, 32000.0, 31933.0, 31832.0, 31766.0, 31720.0, 31664.0, 31616.0, 31540.0, 31444.0, 31332.0, 31308.0, 31200.0, 30980.0, 30882.0, 30826.0, 30826.0, 30856.0, 30816.0, 30774.0, 30776.0, 30873.0, 30849.0, 30840.0, 30812.0, 30744.0, 30804.0, 30824.0, 31083.0, 31914.0, 33104.0, 34220.0, 34920.0, 35188.0, 35124.0, 34880.0, 34576.0, 34324.0, 33968.0, 33572.0, 33212.0, 32904.0, 32744.0, 32660.0, 32808.0, 32904.0, 32984.0, 32992.0, 32912.0, 32736.0, 32733.0, 32664.0, 32504.0, 32330.0, 32136.0, 31920.0, 31728.0, 31642.0, 31648.0, 31612.0, 31608.0, 31628.0, 31560.0, 31504.0, 31472.0, 31412.0, 31418.0, 31360.0, 31308.0, 31250.0, 31172.0, 31088.0, 30976.0, 30940.0, 31280.0, 32416.0, 33808.0, 35232.0, 36056.0, 36408.0, 36432.0, 36180.0, 35936.0, 35640.0, 35440.0, 35264.0, 34968.0, 34608.0, 34120.0, 33888.0, 33696.0, 33676.0, 33696.0, 33672.0, 33644.0, 33548.0, 33428.0, 33276.0, 33104.0, 32868.0, 32616.0, 32477.0, 32296.0, 32144.0, 31944.0, 31890.0, 31832.0, 31780.0, 31720.0, 31688.0, 31672.0, 31552.0, 31528.0, 31424.0, 31360.0, 31256.0, 31252.0, 31144.0, 31041.0, 30976.0, 30937.0, 30884.0, 30824.0, 30800.0, 30784.0, 30776.0, 30756.0, 30752.0, 30737.0, 30720.0, 30752.0, 30738.0, 30642.0, 30656.0, 30976.0, 32036.0, 33544.0, 34968.0, 35884.0, 36296.0, 36272.0, 35948.0, 35530.0, 35176.0, 34834.0, 34520.0, 34288.0, 33944.0, 33656.0, 33386.0, 33280.0, 33328.0, 33420.0, 33552.0, 33536.0, 33356.0, 33272.0, 33016.0, 32864.0, 32708.0, 32493.0, 32320.0, 32144.0, 32008.0, 31914.0, 31820.0, 31756.0, 31700.0, 31648.0, 31628.0, 31576.0, 31544.0, 31492.0, 31452.0, 31392.0, 31332.0, 31216.0, 31242.0, 31200.0, 31136.0, 31104.0, 31068.0, 31033.0, 31000.0, 30936.0, 30882.0, 30840.0, 30756.0, 30820.0, 30777.0, 30704.0, 30700.0, 30672.0, 30668.0, 30965.0, 31932.0, 33372.0, 34968.0, 36052.0, 36560.0, 36524.0, 36244.0, 35800.0, 35276.0, 34965.0, 34660.0, 34356.0, 34056.0, 33696.0, 33364.0, 33200.0, 33152.0, 33248.0, 33320.0, 33384.0, 33360.0, 33184.0, 33068.0, 32832.0, 32632.0, 32420.0, 32272.0, 32088.0, 31944.0, 31784.0, 31732.0, 31632.0, 31496.0, 31416.0, 31416.0, 31356.0, 31312.0, 31260.0, 31224.0, 31200.0, 31209.0, 31172.0, 31109.0, 31080.0, 31041.0, 30984.0, 30944.0, 30868.0, 30912.0, 30868.0, 30834.0, 30828.0, 30764.0, 30714.0, 30652.0, 30696.0, 30688.0, 30784.0, 31476.0, 32776.0, 34220.0, 35572.0, 36265.0, 36424.0, 36244.0, 35848.0, 35372.0, 34882.0, 34448.0, 34202.0, 33816.0, 33424.0, 33032.0, 32824.0, 32874.0, 33028.0, 33240.0, 33364.0, 33368.0, 33284.0, 33160.0, 32938.0, 32702.0, 32487.0, 32304.0, 32116.0, 31936.0, 31792.0, 31624.0, 31493.0, 31472.0, 31384.0, 31408.0, 31336.0, 31340.0, 31284.0, 31284.0, 31264.0, 31266.0, 31224.0, 31188.0, 31156.0, 31136.0, 31064.0, 31056.0, 30996.0, 30952.0, 30888.0, 30816.0, 30776.0, 30808.0, 31180.0, 32268.0, 33748.0, 35188.0, 36128.0, 36536.0, 36484.0, 36160.0, 35638.0, 35308.0, 34920.0, 34568.0, 34232.0, 33740.0, 33428.0, 33148.0, 33056.0, 33088.0, 33248.0, 33346.0, 33424.0, 33336.0, 33300.0, 33072.0, 32888.0, 32704.0, 32476.0, 32306.0, 32128.0, 31996.0, 31860.0, 31790.0, 31644.0, 31625.0, 31572.0, 31568.0, 31496.0, 31505.0, 31420.0, 31456.0, 31416.0, 31386.0, 31368.0, 31360.0, 31300.0, 31264.0, 31208.0, 31188.0, 31132.0, 31104.0, 31050.0, 31004.0, 30968.0, 30898.0, 30890.0, 30976.0, 31474.0, 32708.0, 34282.0, 35520.0, 36484.0, 36760.0, 36596.0, 36208.0, 35748.0, 35292.0, 34864.0, 34448.0, 34096.0, 33809.0, 33456.0, 33243.0, 33200.0, 33332.0, 33516.0, 33632.0, 33640.0, 33592.0, 33536.0, 33344.0, 33120.0, 32872.0, 32616.0, 32496.0, 32328.0, 32152.0, 32032.0, 31968.0, 31904.0, 31825.0, 31860.0, 31810.0, 31816.0, 31784.0, 31769.0, 31720.0, 31721.0, 31716.0, 31634.0, 31648.0, 31600.0, 31544.0, 31488.0, 31424.0, 31304.0, 31274.0, 31184.0, 31264.0, 31524.0, 32420.0, 33794.0, 35152.0, 35962.0, 36396.0, 36304.0, 35844.0, 35488.0, 34952.0, 34624.0, 34256.0, 33832.0, 33418.0, 33088.0, 32970.0, 33040.0, 33280.0, 33512.0, 33664.0, 33728.0, 33616.0, 33440.0, 33256.0, 32948.0, 32792.0, 32616.0, 32476.0, 32300.0, 32184.0, 32020.0, 32020.0, 31972.0, 31936.0, 31936.0, 31920.0, 31868.0, 31864.0, 31848.0, 31836.0, 31792.0, 31744.0, 31680.0, 31656.0, 31636.0, 31584.0, 31544.0, 31496.0, 31465.0, 31428.0, 31338.0, 31340.0, 31284.0, 31250.0, 31321.0, 31784.0, 32952.0, 34426.0, 35908.0, 36816.0, 37144.0, 37016.0, 36672.0, 36220.0, 35800.0, 35480.0, 35168.0, 34890.0, 34460.0, 34228.0, 33960.0, 33884.0, 34098.0, 34220.0, 34308.0, 34340.0, 34240.0, 33976.0, 33780.0, 33652.0, 33444.0, 33224.0, 33068.0, 32896.0, 32776.0, 32688.0, 32600.0, 32540.0, 32528.0, 32476.0, 32416.0, 32424.0, 32384.0, 32282.0, 32314.0, 32276.0, 32232.0, 32176.0, 32152.0, 32020.0, 32010.0, 31922.0, 31900.0, 31864.0, 31812.0, 31752.0, 31740.0, 31696.0, 31660.0, 31588.0, 31577.0, 31596.0, 31592.0, 31490.0, 31529.0, 31496.0, 31528.0, 31808.0, 32926.0, 34360.0, 35880.0, 36912.0, 37288.0, 37248.0, 36928.0, 36512.0, 36075.0, 35714.0, 35352.0, 35168.0, 34912.0, 34533.0, 34376.0, 34320.0, 34284.0, 34528.0, 34649.0, 34692.0, 34616.0, 34393.0, 34280.0, 34050.0, 33856.0, 33616.0, 33408.0, 33320.0, 33162.0, 33056.0, 32956.0, 32932.0, 32880.0, 32848.0, 32824.0, 32796.0, 32752.0, 32664.0, 32681.0, 32656.0, 32584.0, 32540.0, 32498.0, 32426.0, 32372.0, 32264.0, 32234.0, 32129.0, 32130.0, 32060.0, 32084.0, 32028.0, 31986.0, 31944.0, 31872.0, 31824.0, 31753.0, 31800.0, 32076.0, 32928.0, 34268.0, 35536.0, 36294.0, 36722.0, 36708.0, 36424.0, 35940.0, 35618.0, 35232.0, 34872.0, 34520.0, 34106.0, 33824.0, 33624.0, 33777.0, 34064.0, 34308.0, 34388.0, 34368.0, 34248.0, 34132.0, 33896.0, 33688.0, 33448.0, 33280.0, 33096.0, 32930.0, 32824.0, 32704.0, 32602.0, 32545.0, 32576.0, 32576.0, 32536.0, 32512.0, 32481.0, 32472.0, 32396.0, 32348.0, 32296.0, 32196.0, 32184.0, 32089.0, 32093.0, 32032.0, 31968.0, 31914.0, 31884.0, 31848.0, 31832.0, 31792.0, 31760.0, 31748.0, 31700.0, 31672.0, 31600.0, 31612.0, 31578.0, 31652.0, 32296.0, 33640.0, 35204.0, 36544.0, 37188.0, 37464.0, 37200.0, 36888.0, 36464.0, 36060.0, 35792.0, 35480.0, 35400.0, 35072.0, 34760.0, 34632.0, 34532.0, 34708.0, 34820.0, 34888.0, 34868.0, 34736.0, 34568.0, 34268.0, 34120.0, 33924.0, 33732.0, 33536.0, 33356.0, 33240.0, 33132.0, 33056.0, 32988.0, 32904.0, 32840.0, 32840.0, 32818.0, 32778.0, 32744.0, 32704.0, 32633.0, 32577.0, 32504.0, 32396.0, 32392.0, 32328.0, 32244.0, 32172.0, 32128.0, 32080.0, 32050.0, 31992.0, 31968.0, 31932.0, 31864.0, 31808.0, 31824.0, 31764.0, 31712.0, 31688.0, 31712.0, 32000.0, 32984.0, 34376.0, 35699.0, 36672.0, 37072.0, 37004.0, 36569.0, 36312.0, 35848.0, 35496.0, 35112.0, 34904.0, 34528.0, 34204.0, 34104.0, 34136.0, 34368.0, 34616.0, 34840.0, 34888.0, 34736.0, 34665.0, 34506.0, 34240.0, 34065.0, 33840.0, 33664.0, 33512.0, 33368.0, 33237.0, 33160.0, 33112.0, 33068.0, 33024.0, 32976.0, 32920.0, 32960.0, 32900.0, 32884.0, 32840.0, 32708.0, 32696.0, 32641.0, 32596.0, 32496.0, 32421.0, 32316.0, 32240.0, 32124.0, 32124.0, 32100.0, 32700.0, 33828.0, 35248.0, 36402.0, 37152.0, 37321.0, 37196.0, 36880.0, 36468.0, 36116.0, 35748.0, 35624.0, 35320.0, 35008.0, 34752.0, 34700.0, 34776.0, 35020.0, 35184.0, 35274.0, 35176.0, 35056.0, 34920.0, 34720.0, 34508.0, 34220.0, 34104.0, 33936.0, 33772.0, 33644.0, 33584.0, 33504.0, 33410.0, 33428.0, 33416.0, 33352.0, 33300.0, 33168.0, 33132.0, 33088.0, 32984.0, 32920.0, 32824.0, 32770.0, 32680.0, 32612.0, 32556.0, 32440.0, 32364.0, 32384.0, 32252.0, 32241.0, 32133.0, 32104.0, 32061.0, 32193.0, 32840.0, 34226.0, 35696.0, 36820.0, 37480.0, 37672.0, 37456.0, 37132.0, 36804.0, 36544.0, 36252.0, 36224.0, 36056.0, 35842.0, 35560.0, 35372.0, 35384.0, 35496.0, 35538.0, 35576.0, 35524.0, 35384.0, 35212.0, 35001.0, 34696.0, 34584.0, 34308.0, 34202.0, 34024.0, 33896.0, 33792.0, 33652.0, 33624.0, 33536.0, 33408.0, 33440.0, 33402.0, 33302.0, 33226.0, 33184.0, 33096.0, 33012.0, 32916.0, 32801.0, 32680.0, 32652.0, 32572.0, 32400.0, 32388.0, 32330.0, 32256.0, 32208.0, 32168.0, 32128.0, 32052.0, 32020.0, 31968.0, 31904.0, 31852.0, 31804.0, 31720.0, 31688.0, 32032.0, 33040.0, 34428.0, 35672.0, 36660.0, 37033.0, 36936.0, 36696.0, 36312.0, 36004.0, 35752.0, 35584.0, 35362.0, 35096.0, 34744.0, 34666.0, 34620.0, 34756.0, 34944.0, 35028.0, 35048.0, 34866.0, 34720.0, 34604.0, 34370.0, 34105.0, 33977.0, 33784.0, 33636.0, 33500.0, 33388.0, 33312.0, 33298.0, 33237.0, 33194.0, 33146.0, 33033.0, 33016.0, 32972.0, 32896.0, 32832.0, 32712.0, 32620.0, 32548.0, 32448.0, 32384.0, 32329.0, 32235.0, 32092.0, 32060.0, 32000.0, 31940.0, 31840.0, 31800.0, 31738.0, 31692.0, 31888.0, 32588.0, 33740.0, 35108.0, 36068.0, 36513.0, 36576.0, 36306.0, 35940.0, 35594.0, 35368.0, 35272.0, 35072.0, 34804.0, 34516.0, 34320.0, 34330.0, 34440.0, 34576.0, 34724.0, 34676.0, 34580.0, 34418.0, 34208.0, 34020.0, 33824.0, 33632.0, 33448.0, 33280.0, 33120.0, 33044.0, 32928.0, 32908.0, 32872.0, 32810.0, 32764.0, 32688.0, 32618.0, 32552.0, 32480.0, 32384.0, 32324.0, 32229.0, 32148.0, 32064.0, 32000.0, 31896.0, 31852.0, 31728.0, 31673.0, 31610.0, 31568.0, 31524.0, 31530.0, 31464.0, 31400.0, 31384.0, 31344.0, 31276.0, 31188.0, 31156.0, 31136.0, 31268.0, 31948.0, 33248.0, 34716.0, 35844.0, 36468.0, 36473.0, 36345.0, 36036.0, 35658.0, 35408.0, 35168.0, 35168.0, 35032.0, 34844.0, 34560.0, 34348.0, 34352.0, 34432.0, 34404.0, 34516.0, 34440.0, 34340.0, 34136.0, 33936.0, 33760.0, 33576.0, 33400.0, 33210.0, 33024.0, 32928.0, 32836.0, 32712.0, 32680.0, 32640.0, 32547.0, 32480.0, 32486.0, 32408.0, 32356.0, 32274.0, 32192.0, 32109.0, 32044.0, 31952.0, 31888.0, 31810.0, 31728.0, 31672.0, 31600.0, 31553.0, 31488.0, 31444.0, 31416.0, 31353.0, 31276.0, 31304.0, 31238.0, 31152.0, 31140.0, 31048.0, 31009.0, 30996.0, 30996.0, 31348.0, 32268.0, 33541.0, 34816.0, 35537.0, 35912.0, 35764.0, 35448.0, 35204.0, 34768.0, 34560.0, 34330.0, 34088.0, 33784.0, 33504.0, 33300.0, 33360.0, 33596.0, 33792.0, 33965.0, 34000.0, 33896.0, 33656.0, 33536.0, 33304.0, 33088.0, 32888.0, 32672.0, 32546.0, 32376.0, 32360.0, 32300.0, 32224.0, 32236.0, 32216.0, 32186.0, 32140.0, 32100.0, 31984.0, 31988.0, 31924.0, 31802.0, 31784.0, 31711.0, 31600.0, 31572.0, 31488.0, 31384.0, 31304.0, 31216.0, 31164.0, 31177.0, 31306.0, 32000.0, 33124.0, 34256.0, 35036.0, 35412.0, 35320.0, 35072.0, 34616.0, 34336.0, 34012.0, 33644.0, 33320.0, 33026.0, 32780.0, 32648.0, 32842.0, 33072.0, 33256.0, 33348.0, 33242.0, 33125.0, 32960.0, 32746.0, 32515.0, 32404.0, 32192.0, 32112.0, 31992.0, 31876.0, 31746.0, 31712.0, 31660.0, 31656.0, 31596.0, 31540.0, 31569.0, 31512.0, 31532.0, 31474.0, 31500.0, 31400.0, 31332.0, 31321.0, 31240.0, 31176.0, 31104.0, 31040.0, 31008.0, 30906.0, 30890.0, 30868.0, 30824.0, 30960.0, 31692.0, 33072.0, 34498.0, 35570.0, 36024.0, 35984.0, 35616.0, 35156.0, 34664.0, 34248.0, 33996.0, 33632.0, 33312.0, 33056.0, 33028.0, 33152.0, 33376.0, 33550.0, 33565.0, 33544.0, 33418.0, 33248.0, 32948.0, 32832.0, 32608.0, 32456.0, 32284.0, 32144.0, 32044.0, 31972.0, 31938.0, 31864.0, 31848.0, 31896.0, 31858.0, 31808.0, 31712.0, 31652.0, 31532.0, 31532.0, 31492.0, 31800.0, 32864.0, 34369.0, 35752.0, 36632.0, 36992.0, 36848.0, 36568.0, 36188.0, 36032.0, 35800.0, 35744.0, 35596.0, 35352.0, 34912.0, 34728.0, 34600.0, 34532.0, 34652.0, 34585.0, 34470.0, 34400.0, 34244.0, 33960.0, 33756.0, 33616.0, 33433.0, 33264.0, 33096.0, 32952.0, 32836.0, 32784.0, 32708.0, 32636.0, 32616.0, 32516.0, 32404.0, 32388.0, 32360.0, 32260.0, 32160.0, 32068.0, 32000.0, 31856.0, 31808.0, 31716.0, 31672.0, 31576.0, 31504.0, 31488.0, 31449.0, 31405.0, 31346.0, 31300.0, 31272.0, 31227.0, 31122.0, 31132.0, 31120.0, 31044.0, 31024.0, 30988.0, 30960.0, 30872.0, 30840.0, 30820.0, 30842.0, 30800.0, 30760.0, 30730.0, 30672.0, 30648.0, 30637.0, 30788.0, 31616.0, 33204.0, 34876.0, 36036.0, 36776.0, 36772.0, 36436.0, 35930.0, 35420.0, 35136.0, 34872.0, 34696.0, 34416.0, 34120.0, 33904.0, 33802.0, 34008.0, 34180.0, 34273.0, 34210.0, 34160.0, 33872.0, 33712.0, 33504.0, 33252.0, 33092.0, 32876.0, 32724.0, 32610.0, 32500.0, 32424.0, 32384.0, 32360.0, 32304.0, 32270.0, 32200.0, 32152.0, 32073.0, 31956.0, 31924.0, 31786.0, 31768.0, 31684.0, 31584.0, 31516.0, 31492.0, 31424.0, 31381.0, 31332.0, 31257.0, 31224.0, 31156.0, 31108.0, 31008.0, 30952.0, 31032.0, 31346.0, 32232.0, 33632.0, 35024.0, 35928.0, 36297.0, 36208.0, 35868.0, 35572.0, 35233.0, 34912.0, 34865.0, 34624.0, 34396.0, 34104.0, 33860.0, 33796.0, 33864.0, 33936.0, 33936.0, 33876.0, 33756.0, 33572.0, 33416.0, 33120.0, 32972.0, 32776.0, 32664.0, 32504.0, 32376.0, 32288.0, 32221.0, 32184.0, 32138.0, 32096.0, 32048.0, 31904.0, 31937.0, 31844.0, 31764.0, 31696.0, 31624.0, 31525.0, 31468.0, 31396.0, 31348.0, 31292.0, 31178.0, 31176.0, 31120.0, 31074.0, 31024.0, 30984.0, 30908.0, 30948.0, 30928.0, 30844.0, 30836.0, 30824.0, 30810.0, 30730.0, 30712.0, 30672.0, 30640.0, 30600.0, 30921.0, 31872.0, 33304.0, 34720.0, 35808.0, 36192.0, 36136.0, 35880.0, 35476.0, 35136.0, 34896.0, 34776.0, 34684.0, 34496.0, 34216.0, 33952.0, 33784.0, 33792.0, 33856.0, 33849.0, 33732.0, 33716.0, 33580.0, 33336.0, 33178.0, 32960.0, 32792.0, 32584.0, 32384.0, 32360.0, 32192.0, 32153.0, 32092.0, 32072.0, 32040.0, 31993.0, 31936.0, 31870.0, 31844.0, 31800.0, 31720.0, 31604.0, 31600.0, 31508.0, 31416.0, 31416.0, 31332.0, 31290.0, 31232.0, 31180.0, 31148.0, 31104.0, 31074.0, 31048.0, 30976.0, 30954.0, 30968.0, 30920.0, 30892.0, 30880.0, 30860.0, 30818.0, 30813.0, 30708.0, 30684.0, 30666.0, 30608.0, 30556.0, 30576.0, 30632.0, 31088.0, 32128.0, 33584.0, 34794.0, 35514.0, 35736.0, 35392.0, 35050.0, 34729.0, 34352.0, 34120.0, 33836.0, 33656.0, 33392.0, 33144.0, 33012.0, 33072.0, 33232.0, 33392.0, 33472.0, 33420.0, 33288.0, 33064.0, 32888.0, 32684.0, 32468.0, 32312.0, 32146.0, 31988.0, 31920.0, 31780.0, 31800.0, 31748.0, 31712.0, 31686.0, 31674.0, 31612.0, 31642.0, 31570.0, 31456.0, 31468.0, 31416.0, 31312.0, 31266.0, 31196.0, 31096.0, 31080.0, 31040.0, 31008.0, 30944.0, 30930.0, 30893.0, 30892.0, 30840.0, 30784.0, 30730.0, 30688.0, 30668.0, 30708.0, 31250.0, 32344.0, 33674.0, 34760.0, 35384.0, 35496.0, 35284.0, 34909.0, 34489.0, 34220.0, 33944.0, 33764.0, 33504.0, 33176.0, 32992.0, 32792.0, 32872.0, 32984.0, 33024.0, 33148.0, 33060.0, 32876.0, 32784.0, 32596.0, 32344.0, 32200.0, 31992.0, 31888.0, 31780.0, 31680.0, 31592.0, 31512.0, 31540.0, 31504.0, 31485.0, 31452.0, 31364.0, 31368.0, 31296.0, 31256.0, 31216.0, 31164.0, 31084.0, 31064.0, 31024.0, 31000.0, 30948.0, 30890.0, 30848.0, 30802.0, 30740.0, 30736.0, 30714.0, 30708.0, 30672.0, 30664.0, 30624.0, 30570.0, 30562.0, 30592.0, 30544.0, 30488.0, 30444.0, 30408.0, 30464.0, 30920.0, 32100.0, 33520.0, 34796.0, 35520.0, 35708.0, 35512.0, 35088.0, 34716.0, 34346.0, 34104.0, 33968.0, 33840.0, 33632.0, 33400.0, 33188.0, 33084.0, 33072.0, 33136.0, 33200.0, 33160.0, 33069.0, 32852.0, 32684.0, 32588.0, 32396.0, 32196.0, 32080.0, 31946.0, 31820.0, 31760.0, 31636.0, 31617.0, 31568.0, 31496.0, 31508.0, 31481.0, 31460.0, 31424.0, 31368.0, 31312.0, 31252.0, 31216.0, 31170.0, 31108.0, 31080.0, 31028.0, 30992.0, 30960.0, 30920.0, 30872.0, 30816.0, 30800.0, 30824.0, 30796.0, 30752.0, 30740.0, 30688.0, 30705.0, 30720.0, 30692.0, 30684.0, 30640.0, 30610.0, 30562.0, 30562.0, 30616.0, 30984.0, 32017.0, 33404.0, 34728.0, 35484.0, 35776.0, 35624.0, 35236.0, 34788.0, 34392.0, 34060.0, 33884.0, 33612.0, 33400.0, 33120.0, 32964.0, 32916.0, 33012.0, 33192.0, 33304.0, 33368.0, 33276.0, 33152.0, 32948.0, 32749.0, 32548.0, 32392.0, 32160.0, 32096.0, 31968.0, 31880.0, 31820.0, 31740.0, 31744.0, 31712.0, 31684.0, 31744.0, 31724.0, 31680.0, 31636.0, 31608.0, 31576.0, 31520.0, 31404.0, 31408.0, 31332.0, 31252.0, 31212.0, 31188.0, 31144.0, 31112.0, 31076.0, 31009.0, 30956.0, 30962.0, 30922.0, 30914.0, 30892.0, 30976.0, 31448.0, 32546.0, 33920.0, 35060.0, 35692.0, 35833.0, 35584.0, 35116.0, 34721.0, 34384.0, 34076.0, 33856.0, 33586.0, 33272.0, 33025.0, 32897.0, 32988.0, 33114.0, 33376.0, 33412.0, 33404.0, 33376.0, 33136.0, 32956.0, 32680.0, 32570.0, 32364.0, 32216.0, 32160.0, 32040.0, 31972.0, 31852.0, 31836.0, 31832.0, 31834.0, 31780.0, 31802.0, 31776.0, 31704.0, 31700.0, 31640.0, 31560.0, 31540.0, 31513.0, 31468.0, 31392.0, 31353.0, 31296.0, 31260.0, 31220.0, 31192.0, 31192.0, 31164.0, 31164.0, 31120.0, 31076.0, 31060.0, 31032.0, 30994.0, 30988.0, 31288.0, 32228.0, 33696.0, 35122.0, 36100.0, 36536.0, 36458.0, 36091.0, 35684.0, 35264.0, 34904.0, 34736.0, 34680.0, 34456.0, 34256.0, 33956.0, 33904.0, 33876.0, 33928.0, 34112.0, 34152.0, 34114.0, 33920.0, 33796.0, 33624.0, 33448.0, 33232.0, 33032.0, 32928.0, 32785.0, 32696.0, 32544.0, 32564.0, 32504.0, 32468.0, 32418.0, 32400.0, 32347.0, 32328.0, 32290.0, 32264.0, 32193.0, 32160.0, 32032.0, 32040.0, 31976.0, 31850.0, 31856.0, 31808.0, 31760.0, 31748.0, 31649.0, 31674.0, 31664.0, 31572.0, 31604.0, 31576.0, 31513.0, 31488.0, 31452.0, 31425.0, 31612.0, 32372.0, 33560.0, 35018.0, 35898.0, 36490.0, 36552.0, 36160.0, 35884.0, 35512.0, 35168.0, 34936.0, 34612.0, 34472.0, 34200.0, 33996.0, 33984.0, 34148.0, 34296.0, 34468.0, 34576.0, 34528.0, 34392.0, 34208.0, 34013.0, 33820.0, 33624.0, 33436.0, 33296.0, 33177.0, 33044.0, 32976.0, 32920.0, 32920.0, 32889.0, 32840.0, 32848.0, 32812.0, 32772.0, 32728.0, 32676.0, 32596.0, 32544.0, 32448.0, 32400.0, 32322.0, 32264.0, 32200.0, 32144.0, 32088.0, 31972.0, 31938.0, 31908.0, 31848.0, 31872.0, 32156.0, 33064.0, 34392.0, 35680.0, 36588.0, 37025.0, 36860.0, 36696.0, 36256.0, 36052.0, 35704.0, 35696.0, 35552.0, 35353.0, 34992.0, 34852.0, 34836.0, 34970.0, 35072.0, 35156.0, 35160.0, 35072.0, 34924.0, 34704.0, 34512.0, 34388.0, 34104.0, 34012.0, 33896.0, 33738.0, 33620.0, 33592.0, 33508.0, 33458.0, 33424.0, 33380.0, 33344.0, 33277.0, 33200.0, 33144.0, 33008.0, 32952.0, 32888.0, 32808.0, 32708.0, 32640.0, 32506.0, 32524.0, 32458.0, 32408.0, 32320.0, 32216.0, 32192.0, 32176.0, 32100.0, 31980.0, 31904.0, 31896.0, 31856.0, 31784.0, 32034.0, 32828.0, 34161.0, 35508.0, 36556.0, 37056.0, 37098.0, 36880.0, 36408.0, 36196.0, 35952.0, 35796.0, 35692.0, 35504.0, 35216.0, 34932.0, 34892.0, 34856.0, 34968.0, 35108.0, 35112.0, 35032.0, 34784.0, 34696.0, 34368.0, 34224.0, 34076.0, 33898.0, 33760.0, 33582.0, 33384.0, 33394.0, 33312.0, 33272.0, 33108.0, 33132.0, 33032.0, 33012.0, 32928.0, 32840.0, 32673.0, 32628.0, 32568.0, 32472.0, 32386.0, 32320.0, 32220.0, 32146.0, 32020.0, 31956.0, 31936.0, 31880.0, 31788.0, 31776.0, 31720.0, 31640.0, 31468.0, 31472.0, 31376.0, 31354.0, 31424.0, 31828.0, 32800.0, 34052.0, 35122.0, 35656.0, 35832.0, 35696.0, 35340.0, 34964.0, 34624.0, 34370.0, 34114.0, 33840.0, 33492.0, 33168.0, 33196.0, 33322.0, 33520.0, 33672.0, 33716.0, 33640.0, 33522.0, 33320.0, 33092.0, 32861.0, 32684.0, 32496.0, 32328.0, 32194.0, 32088.0, 31960.0, 31962.0, 31864.0, 31864.0, 31834.0, 31784.0, 31746.0, 31700.0, 31596.0, 31536.0, 31517.0, 31448.0, 31360.0, 31248.0, 31248.0, 31193.0, 31072.0, 31088.0, 31024.0, 30956.0, 30913.0, 30860.0, 30792.0, 30736.0, 30720.0, 30728.0, 30920.0, 31704.0, 33012.0, 34208.0, 35164.0, 35481.0, 35308.0, 35100.0, 34688.0, 34348.0, 34080.0, 33881.0, 33764.0, 33536.0, 33180.0, 33012.0, 32928.0, 32960.0, 32992.0, 33064.0, 33120.0, 32952.0, 32860.0, 32688.0, 32476.0, 32234.0, 32131.0, 31974.0, 31852.0, 31664.0, 31648.0, 31568.0, 31516.0, 31468.0, 31402.0, 31424.0, 31408.0, 31356.0, 31304.0, 31236.0, 31136.0, 31126.0, 31064.0, 31008.0, 30928.0, 30904.0, 30848.0, 30808.0, 30764.0, 30708.0, 30658.0, 30633.0, 30624.0, 30600.0, 30568.0, 30564.0, 30512.0, 30480.0, 30464.0, 30428.0, 30388.0, 30344.0, 30368.0, 30536.0, 31352.0, 32624.0, 33944.0, 35017.0, 35442.0, 35392.0, 35072.0, 34680.0, 34280.0, 33952.0, 33760.0, 33632.0, 33416.0, 33178.0, 32962.0, 32749.0, 32788.0, 32892.0, 32976.0, 33004.0, 32912.0, 32816.0, 32648.0, 32448.0, 32282.0, 32112.0, 31960.0, 31808.0, 31676.0, 31576.0, 31504.0, 31466.0, 31357.0, 31368.0, 31312.0, 31336.0, 31338.0, 31296.0, 31276.0, 31225.0, 31204.0, 31148.0, 31096.0, 31040.0, 30944.0, 30914.0, 30880.0, 30856.0, 30816.0, 30784.0, 30744.0, 30720.0, 30720.0, 30712.0, 30708.0, 30668.0, 30657.0, 30648.0, 30608.0, 30624.0, 30896.0, 31728.0, 32952.0, 34120.0, 34864.0, 35112.0, 34964.0, 34496.0, 34178.0, 33800.0, 33448.0, 33184.0, 32961.0, 32640.0, 32288.0, 32232.0, 32328.0, 32512.0, 32704.0, 32826.0, 32793.0, 32728.0, 32568.0, 32332.0, 32156.0, 31952.0, 31776.0, 31648.0, 31500.0, 31360.0, 31360.0, 31336.0, 31312.0, 31264.0, 31296.0, 31250.0, 31248.0, 31228.0, 31232.0, 31210.0, 31152.0, 31064.0, 31088.0, 31008.0, 30948.0, 30945.0, 30864.0, 30888.0, 30852.0, 30828.0, 30792.0, 30784.0, 30784.0, 30768.0, 30736.0, 30692.0, 30672.0, 30656.0, 30628.0, 30628.0, 30864.0, 31688.0, 33088.0, 34496.0, 35448.0, 35930.0, 35896.0, 35536.0, 35064.0, 34684.0, 34332.0, 34096.0, 33940.0, 33730.0, 33524.0, 33208.0, 33044.0, 33098.0, 33184.0, 33280.0, 33328.0, 33264.0, 33140.0, 32960.0, 32768.0, 32600.0, 32440.0, 32280.0, 32129.0, 31936.0, 31882.0, 31764.0, 31768.0, 31724.0, 31576.0, 31588.0, 31596.0, 31608.0, 31585.0, 31508.0, 31504.0, 31456.0, 31424.0, 31360.0, 31328.0, 31288.0, 31200.0, 31194.0, 31168.0, 31132.0, 31098.0, 31098.0, 31080.0, 31050.0, 31020.0, 30996.0, 30936.0, 30908.0, 30860.0, 30936.0, 31444.0, 32552.0, 33864.0, 35181.0, 35888.0, 36040.0, 35760.0, 35368.0, 34828.0, 34568.0, 34288.0, 34056.0, 33776.0, 33632.0, 33360.0, 33180.0, 33192.0, 33252.0, 33464.0, 33508.0, 33480.0, 33500.0, 33332.0, 33140.0, 32997.0, 32720.0, 32596.0, 32456.0, 32328.0, 32216.0, 32128.0, 32056.0, 31984.0, 32004.0, 32000.0, 31956.0, 31968.0, 31940.0, 31880.0, 31882.0, 31828.0, 31786.0, 31746.0, 31700.0, 31666.0, 31556.0, 31540.0, 31489.0, 31440.0, 31400.0, 31352.0, 31316.0, 31276.0, 31240.0, 31386.0, 32020.0, 33172.0, 34396.0, 35372.0, 35820.0, 35805.0, 35520.0, 35094.0, 34632.0, 34348.0, 34080.0, 33776.0, 33456.0, 33196.0, 33057.0, 33100.0, 33264.0, 33504.0, 33672.0, 33720.0, 33660.0, 33496.0, 33218.0, 33080.0, 32897.0, 32692.0, 32540.0, 32396.0, 32232.0, 32216.0, 32154.0, 32146.0, 32136.0, 32108.0, 32068.0, 32080.0, 32016.0, 32048.0, 31965.0, 31968.0, 31914.0, 31864.0, 31776.0, 31680.0, 31690.0, 31658.0, 31640.0, 31596.0, 31532.0, 31490.0, 31444.0, 31396.0, 31372.0, 31376.0, 31564.0, 32328.0, 33752.0, 35168.0, 36088.0, 36528.0, 36616.0, 36272.0, 35936.0, 35596.0, 35314.0, 35068.0, 35040.0, 34824.0, 34592.0, 34348.0, 34136.0, 34258.0, 34332.0, 34412.0, 34384.0, 34240.0, 34208.0, 34064.0, 33864.0, 33652.0, 33472.0, 33296.0, 33168.0, 33053.0, 32960.0, 32865.0, 32740.0, 32797.0, 32744.0, 32692.0, 32664.0, 32624.0, 32560.0, 32488.0, 32432.0, 32364.0, 32304.0, 32216.0, 32192.0, 32129.0, 32048.0, 32012.0, 31968.0, 31904.0, 31836.0, 31844.0, 31816.0, 31760.0, 31648.0, 31666.0, 31616.0, 31592.0, 31588.0, 31972.0, 32970.0, 34312.0, 35528.0, 36256.0, 36440.0, 36333.0, 35992.0, 35488.0, 35180.0, 34988.0, 34812.0, 34528.0, 34328.0, 34040.0, 33816.0, 33920.0, 34092.0, 34250.0, 34274.0, 34282.0, 34132.0, 33960.0, 33770.0, 33568.0, 33360.0, 33192.0, 32957.0, 32864.0, 32736.0, 32640.0, 32588.0, 32484.0, 32525.0, 32496.0, 32378.0, 32400.0, 32377.0, 32296.0, 32168.0, 32184.0, 32096.0, 32017.0, 31880.0, 31872.0, 31824.0, 31780.0, 31729.0, 31672.0, 31636.0, 31564.0, 31532.0, 31472.0, 31368.0, 31364.0, 31316.0, 31296.0, 31382.0, 31966.0, 33128.0, 34384.0, 35496.0, 36009.0, 36004.0, 35724.0, 35368.0, 35024.0, 34696.0, 34488.0, 34240.0, 34113.0, 33840.0, 33608.0, 33464.0, 33528.0, 33604.0, 33648.0, 33672.0, 33536.0, 33352.0, 33216.0, 33088.0, 32884.0, 32720.0, 32496.0, 32364.0, 32220.0, 32136.0, 32108.0, 32072.0, 32016.0, 31988.0, 31954.0, 31904.0, 31868.0, 31812.0, 31752.0, 31660.0, 31644.0, 31592.0, 31492.0, 31460.0, 31304.0, 31276.0, 31272.0, 31242.0, 31208.0, 31124.0, 31145.0, 31116.0, 31088.0, 31056.0, 31044.0, 31028.0, 31008.0, 30972.0, 30932.0, 30888.0, 30830.0, 30786.0, 30774.0, 30761.0, 31001.0, 31916.0, 33368.0, 34698.0, 35564.0, 35728.0, 35668.0, 35304.0, 34856.0, 34496.0, 34238.0, 34032.0, 33928.0, 33736.0, 33496.0, 33268.0, 33176.0, 33184.0, 33272.0, 33352.0, 33322.0, 33240.0, 33096.0, 32904.0, 32668.0, 32444.0, 32272.0, 32128.0, 32004.0, 31936.0, 31876.0, 31816.0, 31776.0, 31740.0, 31682.0, 31640.0, 31608.0, 31585.0, 31560.0, 31488.0, 31408.0, 31400.0, 31370.0, 31312.0, 31272.0, 31224.0, 31198.0, 31169.0, 31188.0, 31168.0, 31128.0, 31052.0, 31037.0, 30936.0, 30944.0, 30922.0, 30860.0, 30880.0, 30844.0, 30762.0, 30836.0, 31204.0, 32266.0, 33626.0, 34760.0, 35344.0, 35296.0, 34938.0, 34528.0, 34016.0, 33552.0, 33178.0, 32784.0, 32394.0, 32160.0, 32138.0, 32308.0, 32600.0, 32870.0, 32936.0, 32996.0, 32922.0, 32720.0, 32504.0, 32274.0, 32092.0, 31872.0, 31776.0, 31628.0, 31536.0, 31408.0, 31378.0, 31344.0, 31368.0, 31370.0, 31388.0, 31372.0, 31363.0, 31324.0, 31232.0, 31260.0, 31216.0, 31168.0, 31120.0, 31088.0, 31037.0, 30944.0, 30880.0, 30860.0, 30828.0, 30976.0, 31876.0, 33386.0, 35024.0, 36140.0, 36516.0, 36392.0, 35882.0, 35528.0, 35096.0, 34768.0, 34552.0, 34336.0, 34188.0, 33940.0, 33688.0, 33456.0, 33538.0, 33592.0, 33632.0, 33600.0, 33524.0, 33408.0, 33178.0, 33000.0, 32772.0, 32624.0, 32401.0, 32292.0, 32208.0, 32116.0, 32040.0, 31978.0, 31876.0, 31888.0, 31832.0, 31740.0, 31748.0, 31728.0, 31704.0, 31641.0, 31600.0, 31532.0, 31466.0, 31416.0, 31356.0, 31304.0, 31265.0, 31200.0, 31176.0, 31120.0, 31088.0, 31040.0, 31012.0, 30936.0, 30916.0, 30896.0, 30920.0, 30904.0, 30876.0, 30864.0, 30852.0, 30816.0, 30833.0, 30796.0, 30724.0, 30722.0, 30708.0, 30788.0, 31388.0, 32852.0, 34568.0, 36016.0, 36716.0, 36888.0, 36524.0, 35922.0, 35434.0, 34984.0, 34728.0, 34434.0, 34296.0, 34080.0, 33786.0, 33540.0, 33572.0, 33672.0, 33808.0, 33824.0, 33820.0, 33824.0, 33656.0, 33460.0, 33252.0, 32996.0, 32776.0, 32616.0, 32504.0, 32354.0, 32298.0, 32240.0, 32172.0, 32146.0, 32082.0, 32020.0, 32048.0, 32005.0, 31984.0, 31946.0, 31880.0, 31844.0, 31782.0, 31732.0, 31608.0, 31604.0, 31552.0, 31496.0, 31392.0, 31392.0, 31368.0, 31264.0, 31248.0, 31276.0, 31256.0, 31232.0, 31200.0, 31168.0, 31136.0, 31108.0, 31036.0, 31000.0, 31044.0, 31240.0, 32056.0, 33444.0, 34896.0, 35850.0, 36264.0, 36160.0, 35784.0, 35264.0, 34796.0, 34392.0, 34060.0, 33708.0, 33296.0, 33050.0, 32876.0, 32952.0, 33204.0, 33428.0, 33664.0, 33748.0, 33800.0, 33640.0, 33420.0, 33200.0, 32858.0, 32736.0, 32496.0, 32392.0, 32250.0, 32084.0, 32017.0, 32004.0, 31920.0, 31936.0, 31956.0, 31872.0, 31928.0, 31930.0, 31864.0, 31840.0, 31794.0, 31689.0, 31648.0, 31572.0, 31548.0, 31540.0, 31480.0, 31449.0, 31390.0, 31336.0, 31280.0, 31266.0, 31232.0, 31152.0, 31171.0, 31136.0, 31004.0, 30998.0, 31252.0, 32342.0, 33956.0, 35552.0, 36664.0, 37202.0, 37042.0, 36840.0, 36409.0, 35968.0, 35592.0, 35296.0, 35101.0, 34808.0, 34666.0, 34418.0, 34312.0, 34344.0, 34456.0, 34642.0, 34636.0, 34628.0, 34560.0, 34352.0, 34166.0, 33970.0, 33778.0, 33480.0, 33424.0, 33256.0, 33116.0, 33024.0, 32960.0, 32884.0, 32764.0, 32780.0, 32756.0, 32716.0, 32601.0, 32632.0, 32564.0, 32472.0, 32281.0, 32330.0, 32240.0, 32233.0, 32168.0, 32101.0, 32040.0, 31960.0, 31906.0, 31856.0, 31832.0, 31776.0, 31696.0, 31728.0, 31700.0, 31612.0, 31570.0, 31549.0, 31449.0, 31400.0, 31640.0, 32628.0, 34092.0, 35632.0, 36800.0, 37328.0, 37460.0, 37072.0, 36792.0, 36392.0, 36004.0, 35764.0, 35556.0, 35400.0, 35148.0, 34825.0, 34800.0, 34840.0, 35040.0, 35264.0, 35382.0, 35380.0, 35260.0, 35106.0, 34856.0, 34692.0, 34492.0, 34268.0, 34096.0, 33928.0, 33808.0, 33708.0, 33580.0, 33576.0, 33508.0, 33508.0, 33448.0, 33428.0, 33308.0, 33322.0, 33244.0, 33164.0, 33120.0, 33034.0, 32888.0, 32800.0, 32740.0, 32604.0, 32596.0, 32480.0, 32464.0, 32416.0, 32368.0, 32306.0, 32240.0, 32184.0, 32104.0, 32044.0, 31976.0, 31944.0, 32156.0, 32956.0, 34344.0, 35824.0, 36836.0, 37408.0, 37352.0, 37220.0, 36800.0, 36456.0, 36090.0, 35848.0, 35488.0, 35236.0, 35024.0, 34880.0, 34873.0, 34968.0, 35292.0, 35516.0, 35600.0, 35452.0, 35392.0, 35220.0, 35000.0, 34764.0, 34444.0, 34348.0, 34176.0, 34032.0, 33898.0, 33690.0, 33696.0, 33532.0, 33588.0, 33540.0, 33458.0, 33409.0, 33280.0, 33280.0, 33136.0, 33104.0, 33020.0, 32940.0, 32860.0, 32748.0, 32600.0, 32576.0, 32504.0, 32384.0, 32352.0, 32276.0, 32126.0, 32131.0, 32080.0, 32040.0, 32000.0, 31920.0, 31860.0, 31780.0, 31665.0, 31664.0, 31626.0, 31725.0, 32364.0, 33616.0, 35292.0, 36612.0, 37464.0, 37624.0, 37664.0, 37392.0, 37004.0, 36596.0, 36516.0, 36352.0, 36232.0, 35908.0, 35684.0, 35660.0, 35676.0, 35784.0, 35968.0, 36108.0, 36128.0, 35932.0, 35908.0, 35722.0, 35432.0, 35272.0, 35048.0, 34880.0, 34608.0, 34464.0, 34444.0, 34326.0, 34232.0, 34114.0, 34060.0, 33972.0, 33906.0, 33792.0, 33720.0, 33616.0, 33516.0, 33436.0, 33308.0, 33224.0, 33109.0, 32984.0, 32876.0, 32800.0, 32704.0, 32584.0, 32508.0, 32410.0, 32338.0, 32212.0, 32180.0, 32108.0, 32056.0, 31980.0, 31916.0, 31772.0, 31730.0, 31672.0, 31604.0, 31552.0, 31672.0, 32356.0, 33700.0, 35212.0, 36384.0, 37156.0, 37506.0, 37436.0, 37056.0, 36900.0, 36610.0, 36474.0, 36340.0, 36202.0, 35970.0, 35737.0, 35672.0, 35686.0, 35884.0, 36064.0, 36200.0, 36196.0, 36072.0, 35912.0, 35712.0, 35478.0, 35272.0, 35072.0, 34862.0, 34684.0, 34554.0, 34420.0, 34288.0, 34178.0, 34120.0, 34020.0, 33936.0, 33857.0, 33746.0, 33682.0, 33561.0, 33424.0, 33313.0, 33216.0, 33052.0, 32948.0, 32832.0, 32652.0, 32545.0, 32492.0, 32400.0, 32236.0, 32216.0, 32136.0, 32056.0, 31978.0, 31905.0, 31792.0, 31708.0, 31645.0, 31560.0, 31460.0, 31396.0, 31508.0, 32096.0, 33316.0, 34778.0, 36092.0, 36874.0, 37244.0, 37242.0, 37012.0, 36712.0, 36480.0, 36274.0, 36144.0, 35992.0, 35696.0, 35540.0, 35448.0, 35536.0, 35706.0, 35888.0, 35976.0, 35968.0, 35948.0, 35772.0, 35456.0, 35349.0, 35120.0, 34818.0, 34604.0, 34496.0, 34272.0, 34148.0, 34056.0, 33884.0, 33810.0, 33788.0, 33736.0, 33548.0, 33520.0, 33424.0, 33328.0, 33088.0, 33056.0, 32920.0, 32698.0, 32652.0, 32484.0, 32452.0, 32296.0, 32248.0, 32158.0, 32080.0, 31920.0, 31888.0, 31832.0, 31668.0, 31632.0, 31552.0, 31456.0, 31360.0, 31328.0, 31320.0, 31900.0, 32976.0, 34372.0, 35636.0, 36408.0, 36866.0, 36872.0, 36712.0, 36464.0, 36264.0, 36056.0, 35896.0, 35672.0, 35460.0, 35208.0, 35137.0, 35224.0, 35580.0, 35756.0, 35904.0, 35924.0, 35832.0, 35672.0, 35472.0, 35236.0, 34988.0, 34736.0, 34548.0, 34364.0, 34180.0, 33992.0, 33944.0, 33822.0, 33732.0, 33640.0, 33556.0, 33460.0, 33328.0, 33212.0, 33074.0, 32944.0, 32832.0, 32692.0, 32584.0, 32394.0, 32344.0, 32220.0, 32120.0, 31996.0, 31900.0, 31777.0, 31672.0, 31584.0, 31480.0, 31378.0, 31320.0, 31452.0, 32250.0, 33530.0, 34952.0, 36025.0, 36764.0, 37024.0, 36944.0, 36812.0, 36640.0, 36472.0, 36320.0, 36232.0, 36056.0, 35776.0, 35656.0, 35600.0, 35724.0, 35832.0, 35994.0, 36096.0, 36012.0, 35836.0, 35698.0, 35450.0, 35236.0, 34976.0, 34804.0, 34576.0, 34432.0, 34154.0, 33984.0, 33898.0, 33720.0, 33660.0
    ));


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    //permite recuperar una instancia del servicio a través de un punto de acceso
    //conexión entre activity y servicio
    public class MyBinder extends Binder{
        ServiceTemperature getService(){
            return ServiceTemperature.this; // se retorna instancia del servicio
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mHandler = new Handler(Objects.requireNonNull(Looper.myLooper()));
        isPaused = true;
        br = new TempDataReciever();
        IntentFilter filt = new IntentFilter("analogData");
        this.registerReceiver(br, filt);
    }

    //como si estuviese corriendon una tarea largamente
    public void startPretendLongRunningTask(){
        if(runnable == null) {
            runnable = new Runnable() {

                @Override
                public void run() {
//                    calcularTempSensores();
                    calcularTempantiguo();
                    mHandler.postDelayed(this, 1000);
                }
            };
        }
        mHandler.postDelayed(runnable, 1000);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        super.onTaskRemoved(rootIntent);
        unregisterReceiver(br);
        stopSelf();
    }

    public void unPausedPretendLongRunningTask(){
        isPaused = false;
        startPretendLongRunningTask();
    }

    public int getTemp(){
        return temp;
    }

    public class TempDataReciever extends BroadcastReceiver {
        int[] puertos;
        int portbvp, portecg, porttemp, porteda;
        int postemp;

        public TempDataReciever(){
            puertos = new int[]{9, 9, 9, 9}; //puertos van del 1-4, 9 no altera el orden del sort
            SharedPreferences preferences = getSharedPreferences("BVPConfig", Context.MODE_PRIVATE);
            if(preferences != null){
                portbvp = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "0")));
                puertos[0] = portbvp;
            }

            preferences = getSharedPreferences("ECGConfig", Context.MODE_PRIVATE);
            if(preferences != null){
                portecg = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "0")));
                puertos[1] = portecg;
            }

            preferences = getSharedPreferences("TempConfig", Context.MODE_PRIVATE);
            if(preferences != null){
                porttemp = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "0")));
                puertos[2] = porttemp;
            }

            preferences = getSharedPreferences("EDAConfig", Context.MODE_PRIVATE);
            if(preferences != null){
                porteda = Integer.parseInt(Objects.requireNonNull(preferences.getString("port", "0")));
                puertos[3] = porteda;
            }

            Arrays.sort(puertos);
            String sPuertos = Arrays.toString(puertos);
            postemp = sPuertos.indexOf(String.valueOf(porttemp));
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(COLLECT_DATA){
                double temp_value = intent.getExtras().getIntArray("analogData")[postemp];
                data.add(temp_value);
            }
        }
    }


    public void calcularTempSensores() {

        while (data.size() < sample_rate) {

        }
        COLLECT_DATA = false;

        // TODO:IMPLEMENTAR CALCULO

        data.clear();
        COLLECT_DATA = true;
    }


    public void calcularTempantiguo() {
        // TODO: IMPLEMENTAR CALCULO

        Log.d("data size", String.valueOf(data.size()));

        Log.d("collect ", String.valueOf(COLLECT_DATA));


        COLLECT_DATA = false;

        count++;
        value_i = count*sample_rate;
        value_rate = value_rate + 1;

        if(sample_rate*value_rate >= DATA_SIZE) {

            count = 0;
            value_i = 0;
            value_rate = 1;
            COLLECT_DATA = true;
        }
    }

}
