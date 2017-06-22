/**
 * Created by Administrator on 2017/3/9.
 */
var seckill = {
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer:function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        execution:function (seckillId,md5) {
            return '/seckill'+seckillId+'/'+md5+'/execution';
        }
    },
    handleSeckillKill:function (seckillId,node) {
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            if(result && result['success']){
                var exposer = result['data'];
                if(exposer['exposed']){
                    //开启秒杀
                    //获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId,md5);
                    console.log("killUrl:"+killUrl);
                    //不用click,一直绑定，one只绑定一次
                    $('killBtn').one('click',function () {
                        //执行秒杀请求
                        //先禁用按钮
                        $(this).addClass('disabled');
                        //发送秒杀请求
                        $.post(killUrl,{},function (result) {
                            if(result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                //显示秒杀结果
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }

                        });
                    });
                    node.show();
                }else {
                    //客户端有时间偏差，还未开启秒杀
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId,now,start,end);

                }
            }
        })
        
    },
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }

    },
    countdown:function(seckillId,nowTime,startTime,endTime){
        var seckillBox = $('#seckill-box');
        if(nowTime>endTime){
            seckillBox.html('秒杀结束！')
        }else if(nowTime<startTime){
            var killTime = new Date(startTime+1000);
            seckillBox.countdown(killTime,function(event){
                var format = event.strftime('秒杀计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);
            }).on('finish.countdown',function () {
                //获取秒杀地址，执行秒杀
                seckill.handleSeckillKill(seckillId,seckillBox);
            });
        }else{
            //秒杀开始，执行秒杀
            seckill.handleSeckillKill(seckillId,seckillBox);

        }
    },
    detail: {
        init: function (params) {
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            if (!seckill.validatePhone(killPhone)) {
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',
                    keyboard: false
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide()
                            .html('<label class="label label-danger">手机号出错！</label>').show(300);
                    }
                });
            }
            $.get(seckill.URL.now(), {}, function (result) {
                    if (result && result['success']) {
                        var nowTime = result['data'];
                        seckill.countdown(seckillId,nowTime,startTime,endTime);
                    }else{
                        console.log('result:'+result);
                    }
            });
        }
    }
}
