package com.example.vitalsignscheckup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MonitorBloodPressure extends AppCompatActivity {

    int count = 32700; // este valor es ADC
    int count2 = 32600; // este valor es ADC
    int n = 16; //para transformación. Cantidad de canales.

    int m, m2;

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    String dateFormatted = dateFormat.format(date);

    //bvp
    ArrayList<Double> data = new ArrayList<Double>(Arrays.<Double>asList(
            50169.0, 31732.0, 28352.0, 17364.0, 10536.0, 38940.0, 36552.0, 36024.0, 35690.0, 29504.0, 30136.0, 12147.0, 19068.0, 26164.0, 26104.0, 19086.0, 28112.0, 31092.0, 24904.0, 17976.0, 28928.0, 43340.0, 45208.0, 53024.0, 45952.0, 26544.0, 13152.0, 15841.0, 25685.0, 30468.0, 36453.0, 37096.0, 40240.0, 32924.0, 34224.0, 36564.0, 36016.0, 34056.0, 33033.0, 32500.0, 34568.0, 32050.0, 29664.0, 33424.0, 35105.0, 34909.0, 37408.0, 32756.0, 32248.0, 32128.0, 34576.0, 27752.0, 41844.0, 5803.0, 12768.0, 17828.0, 21516.0, 65512.0, 65512.0, 57536.0, 51508.0, 47016.0, 43628.0, 41068.0, 39120.0, 37642.0, 36460.0, 30156.0, 27037.0, 29488.0, 31641.0, 24856.0, 23776.0, 15940.0, 17282.0, 21600.0, 36524.0, 22156.0, 19792.0, 14644.0, 27257.0, 58768.0, 32540.0, 60000.0, 53216.0, 48216.0, 27580.0, 23.0, 4533.0, 11470.0, 16644.0, 20544.0, 23490.0, 25698.0, 27380.0, 58936.0, 24084.0, 45904.0, 62584.0, 34056.0, 14745.0, 19349.0, 30704.0, 50324.0, 50784.0, 42754.0, 7712.0, 22196.0, 26904.0, 17300.0, 61160.0, 65516.0, 63684.0, 56100.0, 44708.0, 48348.0, 45536.0, 42524.0, 40324.0, 23626.0, 7565.0, 21.0, 2385.0, 9812.0, 20794.0, 62304.0, 65522.0, 4155.0, 10890.0, 19076.0, 65512.0, 64780.0, 52760.0, 60512.0, 22.0, 7743.0, 13985.0, 65520.0, 63616.0, 24474.0, 61000.0, 45700.0, 22.0, 5788.0, 65528.0, 61324.0, 54480.0, 4417.0, 44796.0, 53314.0, 24.0, 21208.0, 61668.0, 6524.0, 5730.0, 9560.0, 48112.0, 61610.0, 47840.0, 21.0, 51832.0, 59944.0, 52193.0, 34692.0, 24705.0, 24.0, 5722.0, 12392.0, 65512.0, 4683.0, 11866.0, 65520.0, 10516.0, 65513.0, 18109.0, 8229.0, 14450.0, 18954.0, 65520.0, 60304.0, 52904.0, 20.0, 304.0, 46252.0, 65524.0, 349.0, 65522.0, 1249.0, 65516.0, 64876.0, 48904.0, 23.0, 15762.0, 54088.0, 17859.0, 5340.0, 12193.0, 27084.0, 32785.0, 22461.0, 65516.0, 63592.0, 4923.0, 8633.0, 53584.0, 62024.0, 23.0, 11872.0, 49296.0, 46432.0, 2325.0, 10056.0, 15696.0, 30412.0, 63072.0, 60928.0, 55816.0, 55984.0, 50344.0, 19954.0, 15322.0, 24.0, 5161.0, 11984.0, 22592.0, 30844.0, 51016.0, 65528.0, 42436.0, 21444.0, 29326.0, 30528.0, 47744.0, 61200.0, 2570.0, 4701.0, 5461.0, 11984.0, 17129.0, 65512.0, 60804.0, 37204.0, 4934.0, 3738.0, 33664.0, 65520.0, 50388.0, 23.0, 6080.0, 12752.0, 65516.0, 61376.0, 54252.0, 6218.0, 1507.0, 9348.0, 15141.0, 65514.0, 65512.0, 53456.0, 2499.0, 65404.0, 23.0, 5759.0, 12530.0, 13698.0, 64496.0, 2726.0, 65508.0, 60640.0, 53720.0, 23.0, 4634.0, 64812.0, 2643.0, 10268.0, 65516.0, 4252.0, 61176.0, 21.0, 58690.0, 24.0, 7822.0, 65512.0, 62600.0, 55106.0, 50092.0, 22.0, 4435.0, 65512.0, 25.0, 8248.0, 14316.0, 18828.0, 62628.0, 65520.0, 59848.0, 53184.0, 26064.0, 24.0, 5861.0, 12523.0, 17504.0, 20998.0, 21924.0, 40256.0, 54080.0, 36640.0, 33404.0, 65512.0, 58984.0, 49074.0, 25.0, 3366.0, 8080.0, 14185.0, 18745.0, 22176.0, 24732.0, 65520.0, 65528.0, 57476.0, 51466.0, 46972.0, 7806.0, 24.0, 7310.0, 13563.0, 11268.0, 16648.0, 20299.0, 27505.0, 24304.0, 26344.0, 27890.0, 56776.0, 46820.0, 20798.0, 65524.0, 65522.0, 59420.0, 16522.0, 27466.0, 23.0, 5428.0, 55640.0, 29580.0, 36864.0, 5132.0, 49264.0, 48960.0, 5262.0, 12676.0, 15636.0, 19098.0, 22440.0, 33544.0, 25440.0, 36984.0, 65512.0, 65518.0, 59136.0, 52713.0, 18336.0, 23.0, 4404.0, 65520.0, 62592.0, 23265.0, 25944.0, 37217.0, 20767.0, 33360.0, 44589.0, 7748.0, 16950.0, 17476.0, 21212.0, 46972.0, 32848.0, 57189.0, 54528.0, 50028.0, 60436.0, 36624.0, 23.0, 5328.0, 65520.0, 41952.0, 37800.0, 45984.0, 45692.0, 3914.0, 11175.0, 56492.0, 6626.0, 26402.0, 65512.0, 58528.0, 2172.0, 52728.0, 30104.0, 23.0, 57328.0, 22996.0, 24.0, 4128.0, 63632.0, 63216.0, 24.0, 8148.0, 14248.0, 18776.0, 22180.0, 34224.0, 51940.0, 65528.0, 64472.0, 56624.0, 50816.0, 46496.0, 43233.0, 40765.0, 39050.0, 37636.0, 37200.0, 36329.0, 32560.0, 3286.0, 7656.0, 13089.0, 12637.0, 8310.0, 14612.0, 24668.0, 31596.0, 38124.0, 38137.0, 22868.0, 31144.0, 40829.0, 45184.0, 43116.0, 55200.0, 65528.0, 39049.0, 52618.0, 50752.0, 46440.0, 30896.0, 24.0, 3738.0, 13234.0, 37134.0, 56234.0, 28912.0, 47188.0, 21946.0, 7054.0, 13533.0, 18240.0, 31084.0, 23248.0, 46692.0, 65520.0, 65520.0, 48444.0, 49928.0, 22717.0, 23.0, 5099.0, 20026.0, 44745.0, 54352.0, 31470.0, 32528.0, 53140.0, 39177.0, 32286.0, 30348.0, 37824.0, 16349.0, 30568.0, 40554.0, 25040.0, 19580.0, 12755.0, 25114.0, 31730.0, 25420.0, 15912.0, 20052.0, 49308.0, 20624.0, 16250.0, 17988.0, 31268.0, 65512.0, 64832.0, 23.0, 40868.0, 4108.0, 11295.0, 16648.0, 20640.0, 65504.0, 63020.0, 55532.0, 50016.0, 45888.0, 42776.0, 32676.0, 39308.0, 37768.0, 5150.0, 23.0, 7758.0, 59528.0, 59145.0, 55744.0, 50528.0, 46200.0, 33696.0, 21.0, 32996.0, 8167.0, 20728.0, 37460.0, 63528.0, 36852.0, 37736.0, 7572.0, 13857.0, 55320.0, 65516.0, 35884.0, 30028.0, 30112.0, 46276.0, 30368.0, 30912.0, 19858.0, 5208.0, 27586.0, 23548.0, 65512.0, 56472.0, 28760.0, 19444.0, 20666.0, 41032.0, 33125.0, 26464.0, 20276.0, 11172.0, 29004.0, 38592.0, 42180.0, 31009.0, 23761.0, 17442.0, 20260.0, 35625.0, 24128.0, 48916.0, 65528.0, 48776.0, 61720.0, 54596.0, 49328.0, 31512.0, 47476.0, 25.0, 23.0, 17400.0, 8197.0, 14226.0, 18744.0, 27328.0, 25808.0, 24962.0, 42710.0, 25893.0, 27390.0, 55764.0, 63340.0, 61985.0, 35984.0, 39764.0, 53596.0, 33904.0, 21308.0, 47836.0, 26572.0, 25124.0, 14821.0, 41336.0, 36088.0, 44100.0, 21878.0, 7048.0, 10237.0, 13560.0, 31546.0, 26506.0, 20962.0, 24944.0, 23516.0, 34472.0, 65512.0, 5475.0, 20668.0, 15346.0, 19714.0, 65520.0, 65520.0, 58732.0, 9413.0, 59112.0, 52608.0, 47848.0, 13436.0, 23.0, 62880.0, 24.0, 4419.0, 11417.0, 57136.0, 43884.0, 13192.0, 15092.0, 19424.0, 53991.0, 65512.0, 64356.0, 56528.0, 50740.0, 46400.0, 27168.0, 27520.0, 26112.0, 29704.0, 15576.0, 14152.0, 23110.0, 41874.0, 42284.0, 32634.0, 43188.0, 42088.0, 27097.0, 15321.0, 16458.0, 32514.0, 10952.0, 16308.0, 20316.0, 23328.0, 37980.0, 65512.0, 43715.0, 64104.0, 37308.0, 34856.0, 21048.0, 24236.0, 49858.0, 24540.0, 52292.0, 47560.0, 21974.0, 24.0, 6698.0, 13090.0, 14722.0, 65528.0, 64504.0, 56580.0, 22.0, 6465.0, 64608.0, 52402.0, 40790.0, 56228.0, 41752.0, 22.0, 6265.0, 65512.0, 63040.0, 23.0, 45058.0, 57332.0, 15785.0, 63824.0, 1429.0, 65512.0, 61552.0, 3462.0, 65518.0, 60416.0, 51608.0, 49408.0, 45408.0, 23.0, 4618.0, 8249.0, 14255.0, 18752.0, 23408.0, 48148.0, 22180.0, 40460.0, 34386.0, 21860.0, 46372.0, 65524.0, 62256.0, 23.0, 5972.0, 63252.0, 5987.0, 7522.0, 9469.0, 30752.0, 65513.0, 60608.0, 53722.0, 20904.0, 5713.0, 60232.0, 24.0, 5155.0, 12021.0, 51368.0, 65528.0, 2399.0, 10194.0, 65516.0, 65516.0, 9909.0, 30648.0, 64528.0, 56544.0, 19.0, 2052.0, 9756.0, 4956.0, 12015.0, 17144.0, 65512.0, 65524.0, 60648.0, 53752.0, 48684.0, 23.0, 5698.0, 10900.0, 16261.0, 20282.0, 42656.0, 63768.0, 65512.0, 57492.0, 46608.0, 17540.0, 46976.0, 19728.0, 2754.0, 6976.0, 27258.0, 13880.0, 64328.0, 24.0, 62920.0, 23.0, 1474.0, 6070.0, 2590.0, 49088.0, 58632.0, 23.0, 62528.0, 55008.0, 15484.0, 30.0, 8305.0, 65516.0, 58912.0, 58472.0, 30748.0, 49682.0, 25.0, 5678.0, 12368.0, 17350.0, 21100.0, 23916.0, 57816.0, 65512.0, 64360.0, 56553.0, 51122.0, 46713.0, 27681.0, 8426.0, 14040.0, 19035.0, 24436.0, 34352.0, 34888.0, 24216.0, 6398.0, 12882.0, 65512.0, 62196.0, 54848.0, 24.0, 5241.0, 65516.0, 2585.0, 10258.0, 65520.0, 64360.0, 17842.0, 25929.0, 35732.0, 36016.0, 9118.0, 13104.0, 26014.0, 22216.0, 51540.0, 27068.0, 19460.0, 33888.0, 28832.0, 41568.0, 55178.0, 47956.0, 39508.0, 30560.0, 12752.0, 17804.0, 29468.0, 26341.0, 33600.0, 39876.0, 27240.0, 60676.0, 48825.0, 47028.0, 52484.0, 42532.0, 32320.0, 23664.0, 30529.0, 34314.0, 36246.0, 36468.0, 32532.0, 24696.0, 20768.0, 19848.0, 31640.0, 33052.0, 39081.0, 38864.0, 46569.0, 29862.0, 23890.0, 17376.0, 30816.0, 39988.0, 25920.0, 28292.0, 35936.0, 30344.0, 35124.0, 35220.0, 38732.0, 37404.0, 36692.0, 30330.0, 40392.0, 40816.0, 30224.0, 29184.0, 26296.0, 30273.0, 27296.0, 28286.0, 40880.0, 55188.0, 63240.0, 44900.0, 18848.0, 11064.0, 11556.0, 10976.0, 20316.0, 28729.0, 32864.0, 36688.0, 38808.0, 41171.0, 38508.0, 40050.0, 25769.0, 29464.0, 35800.0, 41690.0, 37232.0, 41824.0, 36545.0, 35720.0, 29920.0, 27290.0, 30516.0, 24592.0, 30232.0, 33402.0, 36060.0, 35108.0, 33122.0, 35305.0, 35256.0, 26832.0, 30213.0, 37700.0, 43668.0, 37098.0, 39336.0, 21038.0, 22356.0, 29890.0, 33232.0, 48336.0, 44528.0, 38024.0, 25832.0, 7297.0, 21286.0, 32796.0, 31072.0, 36178.0, 58996.0, 43424.0, 14640.0, 23408.0, 8850.0, 17036.0, 26432.0, 33248.0, 37404.0, 51912.0, 65516.0, 35844.0, 34860.0, 23.0, 7666.0, 41764.0, 62032.0, 61664.0, 42568.0, 29596.0, 24844.0, 19268.0, 13742.0, 22944.0, 11844.0, 14378.0, 19176.0, 27954.0, 31770.0, 38817.0, 46260.0, 40416.0, 35076.0, 33824.0, 30092.0, 31660.0, 39664.0, 35456.0, 35056.0, 23848.0, 12397.0, 17574.0, 34664.0, 22884.0, 65516.0, 65528.0, 61100.0, 2033.0, 49276.0, 62496.0, 55084.0, 49680.0, 45640.0, 42592.0, 13580.0, 23.0, 12905.0, 17822.0, 22373.0, 65520.0, 25429.0, 60448.0, 53560.0, 36340.0, 18818.0, 16604.0, 50744.0, 21880.0, 5501.0, 3669.0, 10880.0, 16226.0, 20237.0, 34168.0, 43458.0, 42212.0, 45584.0, 37867.0, 28932.0, 41732.0, 41544.0, 32877.0, 44016.0, 49604.0, 48732.0, 11797.0, 13470.0, 22442.0, 8672.0, 65522.0, 59500.0, 23.0, 4602.0, 11649.0, 23120.0, 43208.0, 41724.0, 24448.0, 56824.0, 42148.0, 31338.0, 24848.0, 32020.0, 37860.0, 62432.0, 43049.0, 39400.0, 27300.0, 2046.0, 4336.0, 13826.0, 18696.0, 20376.0, 23382.0, 62016.0, 65524.0, 62524.0, 44232.0, 4491.0, 2795.0, 10315.0, 15865.0, 20020.0, 27120.0, 39032.0, 65528.0, 65520.0, 57864.0, 48480.0, 17868.0, 7509.0, 1784.0, 9510.0, 15245.0, 40720.0, 53848.0, 65520.0, 39512.0, 23418.0, 45772.0, 37384.0, 4953.0, 6328.0, 25172.0, 18420.0, 19200.0, 15588.0, 19813.0, 65520.0, 53676.0, 61332.0, 47344.0, 770.0, 8896.0, 14856.0, 19277.0, 23144.0, 61588.0, 58624.0, 64900.0, 56936.0, 51048.0, 46653.0, 43360.0, 40856.0, 24776.0, 4556.0, 21056.0, 16270.0, 17970.0, 2512.0, 9948.0, 15504.0, 19688.0, 22844.0, 25222.0, 27010.0, 28377.0, 65524.0, 64548.0, 56708.0, 50912.0, 46557.0, 43289.0, 40804.0, 38932.0, 13.0, 46820.0, 24.0, 16300.0, 35562.0, 59120.0, 22.0, 25544.0, 22483.0, 56844.0, 50818.0, 23.0, 704.0, 8616.0, 14496.0, 18912.0, 65508.0, 65520.0, 42576.0, 55716.0, 23.0, 5789.0, 13522.0, 16416.0, 20421.0, 51496.0, 50392.0, 16664.0, 51480.0, 54052.0, 65512.0, 57906.0, 24250.0, 24.0, 50872.0, 29052.0, 31264.0, 33402.0, 37284.0, 39856.0, 26020.0, 40708.0, 14325.0, 5010.0, 11909.0, 17046.0, 20892.0, 56364.0, 45856.0, 65508.0, 61131.0, 24832.0, 1029.0, 40044.0, 53024.0, 58768.0, 33060.0, 662.0, 65528.0, 60464.0, 53488.0, 48442.0, 44692.0, 23.0, 6157.0, 12693.0, 17585.0, 65520.0, 65512.0, 59016.0, 24.0, 58604.0, 25.0, 1704.0, 9474.0, 15208.0, 19512.0, 22732.0, 65520.0, 35628.0, 58232.0, 21.0, 2332.0, 9956.0, 65512.0, 59616.0, 3037.0, 10548.0, 39856.0, 65522.0, 59816.0, 24.0, 5904.0, 65508.0, 59368.0, 52768.0, 47932.0, 44304.0, 41556.0, 23.0, 59336.0, 58692.0, 24.0, 3814.0, 10946.0, 16265.0, 20265.0, 23272.0, 25556.0, 27272.0, 28566.0, 29552.0, 30292.0, 30860.0, 65512.0, 64020.0, 56353.0, 50660.0, 46384.0, 43164.0, 40712.0, 38856.0, 20.0, 1828.0, 65512.0, 60912.0, 19308.0, 685.0, 8736.0, 14587.0, 30109.0, 65512.0, 61048.0, 53476.0, 49000.0, 19.0, 1858.0, 9521.0, 65514.0, 41360.0, 46624.0, 55128.0, 49824.0, 46305.0, 32736.0, 43088.0, 30108.0, 22244.0, 25510.0, 17276.0, 16798.0, 22707.0, 24436.0, 19801.0, 27888.0, 19456.0, 12869.0, 13702.0, 18496.0, 37952.0, 45948.0, 50900.0, 51386.0, 51880.0, 19148.0, 9249.0, 36216.0, 61448.0, 15265.0, 14787.0, 29814.0, 9640.0, 15460.0, 65504.0, 7768.0, 46449.0
    ));





    double tiempos[] = new double[40];
    double tiempos_start[] = new double[40];

    //datos de un sensor
    SignalDetector signalDetector = new SignalDetector();
    //datos del otro sensor
    SignalDetector signalDetector2 = new SignalDetector();

    int lag = 30;
    double threshold = 5;
    double influence = 0;

    //datos de un sensor bvp
    HashMap<String, List> resultsMap = signalDetector.analyzeDataForSignals(data, lag, threshold, influence);
    List<Integer> signalsList = resultsMap.get("signals");

    //datos del otro sensor ecg

    MonitorHeartRate ECG = new MonitorHeartRate();
    ArrayList<Double> data2 = ECG.data;

    HashMap<String, List> resultsMap2 = signalDetector2.analyzeDataForSignals(data2, lag, threshold, influence);
    List<Integer> signalsList2 = resultsMap2.get("signals");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_blood_pressure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.bloodPressureToolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        TextView estado = (TextView)findViewById(R.id.estado);
        estado.setText("presión alta");

        final TextView hist1 = (TextView)findViewById(R.id.hist1);
        final TextView hist2 = (TextView)findViewById(R.id.hist2);

        final TextView textView = (TextView)findViewById(R.id.bp_medicion_mmhg);
        final TextView textView2 = (TextView)findViewById(R.id.bp_medicion_mmhg2);

        //arreglo tiempos
        double arrayTiempos[] = new double[40];

        //ecg
        double valor = 0;
        int dif = 0;
        int ppm = 0;
        int pulsaciones = 0;
        int pulsaciones2 = 0;

        //bvp
        double valor2 = 0;
        int dif2 = 0;
        int ppm2 = 0; //unidad medida sensor bvp
        int pulsaciones_ = 0;
        int pulsaciones_2 = 0;

        int sample_rate = 360;
        int j1 = 0;
        int j2 = 0;
        for (int i = 0; i < signalsList.size(); i++) {
            //ECG
            dif = dif + 1;
            dif2 = dif2 + 1;
            if (i + 1 != signalsList.size()){
                if (signalsList.get(i) == 0){
                    if (signalsList.get(i + 1) == 1){ //halla peak
                        valor = signalsList.get(i);
                        pulsaciones = pulsaciones + 1;
                        pulsaciones2 = pulsaciones2 + 1;
                        //System.out.println("PULSACION EN LA MEDICION: "+ i);
                        System.out.println("VALOR DE DIF " + dif);
                        arrayTiempos[j1] = Double.valueOf(dif);

                        dif = 0;

                    }
                    else if(signalsList.get(i+1) == -1){
                        continue;
                    }
                }
                else if(signalsList.get(i) == -1){
                    if (signalsList.get(i+1) == 1){ //halla peak
                        valor = signalsList.get(i);
                        pulsaciones = pulsaciones + 1;
                        pulsaciones2 = pulsaciones2 + 1;
                        //System.out.println("PULSACION EN LA MEDICION: "+ i);
                        arrayTiempos[j1] = Double.valueOf(dif);

                        System.out.println("VALOR DE DIF " + dif);
                        dif = 0;
                    }
                    else if(signalsList.get(i+1) == 0){
                        continue;
                    }
                }
                else if(signalsList.get(i) == 1){
                    continue;
                }

            }
            if (i % (sample_rate*2) == 0){
                ppm = (ppm + (pulsaciones2*60/2))/2;
                System.out.println("PPM ES: "+ ppm);
                System.out.println("ENTRA AL i = "+ i);
                pulsaciones2 = 0;
            }

            //BVP

            if (i + 1 != signalsList2.size()){
                if (signalsList2.get(i) == 0){
                    if (signalsList2.get(i + 1) == 1){ //halla peak
                        valor = signalsList2.get(i);
                        pulsaciones_ = pulsaciones_ + 1;
                        pulsaciones_2 = pulsaciones_2 + 1;
                        //System.out.println("PULSACION EN LA MEDICION: "+ i);
                        System.out.println("VALOR DE DIF2 " + dif2);
                        arrayTiempos[j1] = (Double.valueOf(dif2) - arrayTiempos[j1])/sample_rate; //ojo con distintos datasets
                        System.out.println("Valor1: " + Double.valueOf(dif2) + " Valor2: " + arrayTiempos[j1]);
                        j1 = j1 + 1;

                        dif2 = 0;
                    }
                    else if(signalsList2.get(i+1) == -1){
                        continue;
                    }
                }
                else if(signalsList2.get(i) == -1){
                    if (signalsList2.get(i+1) == 1){ //halla peak
                        valor2 = signalsList2.get(i);
                        pulsaciones_ = pulsaciones_ + 1;
                        pulsaciones_2 = pulsaciones_2 + 1;
                        //System.out.println("PULSACION EN LA MEDICION: "+ i);
                        System.out.println("VALOR DE DIF2 " + dif2);
                        arrayTiempos[j1] = (Double.valueOf(dif2) - arrayTiempos[j1])/sample_rate; //ojo con distintos datasets

                        j1 = j1 + 1;
                        dif2 = 0;
                    }
                    else if(signalsList2.get(i+1) == 0){
                        continue;
                    }
                }
                else if(signalsList2.get(i) == 1){
                    continue;
                }

            }
            if (i % (sample_rate*2) == 0){
                ppm2 = (ppm2 + (pulsaciones_2*60/2))/2;
                System.out.println("BVP ES: "+ ppm2);
                System.out.println("ENTRA AL i = "+ i);
                pulsaciones_2 = 0;
            }


        }
        System.out.println("CANTIDAD DE PULSACIONES ECG " + pulsaciones);
        m = data.size();
        System.out.println("valor de m es " + m);

        System.out.println("CANTIDAD DE BVP ES " + pulsaciones_);
        m2 = data2.size();
        System.out.println("valor de m2 es " + m2);

        //arrayTiempos = PTT para cada medicion



        //cambia valores en la vista
        Thread t = new Thread(){
            @Override
            public void run(){
                while(!isInterrupted()){
                    try {
                        Thread.sleep(1000);  //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                count++;
                                count2++;

                                double transformed = transformECG(n, count);
                                double transformed2 = transformECG(n, count2);

                                textView.setText(String.valueOf(String.format("%.6f", transformed)));
                                textView2.setText(String.valueOf(String.format("%.6f", transformed2)));

                                date = new Date();
                                dateFormatted = dateFormat.format(date);
                                hist1.setText(dateFormatted + "                     " + String.format("%.8f", transformed) + " mmHg"); //info historial
                                hist2.setText(dateFormatted + "                     " + String.format("%.8f", transformed2) + " mmHg");  //info historial
                                //textView2.setText(String.valueOf(count+80));
                                try {
                                    OutputStreamWriter output = new OutputStreamWriter(openFileOutput("blood_pressure_history.txt", Activity.MODE_APPEND));
                                    output.append(count + "/" + (count+80) +"\n");
                                    output.flush();
                                    output.close();
                                } catch (IOException e) {
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewHistory(View view){
        Intent viewHistoryIntent = new Intent(view.getContext(), checkHistory.class);
        viewHistoryIntent.putExtra("origin", "bloodPressure");
        startActivity(viewHistoryIntent);
    }


    public static double transformECG(int n, int ADC){
        double ECG_V, ECG_mV;
        int G_ECG, VCC;

        VCC = 3;      // operating voltage
        G_ECG = 1000; // sensor gain

        ECG_V = (ADC/Math.pow(2, n) - 0.5)*VCC/G_ECG;

        ECG_mV = ECG_V*1000;

        return ECG_mV;
    }

}