/**
 * Created by Administrator on 2017/3/9.
 */
var seckill = {
    URL : {
        now : function(){
            return '/seckill/time/now';
        }
    },
    validatePhone : function(phone){
        if(phone && phone.length ==11 && isNaN(phone)){
            return true;
        }else{
            return false;
        }

    },
    detail : {
        init : function(params){
            var killPhone = $.cookie('killPhone');
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];

            if(!seckill.validatePhone(killPhone)){
                var killPhoneModal = $.('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,
                    backdrop:'static',
                    keyboard:false
                });
                $('#killPhoneBtn').click(function(){
                    var inputPhone =
                })

            }
        }
    }
}
