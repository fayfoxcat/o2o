$(function() {
	//定义访问后台，获取头条列表以及一级类别列表的URL
    var url = '/o2o/frontend/listmainpageinfo';
    //访问后台获取头条列表、一级类别列表
    $.getJSON(url, function (data) {
        if (data.success) {
        	//获取头条列表
            var headLineList = data.headLineList;
            var swiperHtml = '';
            //遍历头条列表
            headLineList.map(function (item, index) {
                swiperHtml += ''
                            + '<div class="swiper-slide img-wrap">'
                            +      '<img class="banner-img" src="'+ item.lineImg +'" alt="'+ item.lineName +'">'
                            + '</div>';
            });
            $('.swiper-wrapper').html(swiperHtml);
            //设置头条信息播放间隔3秒
            $(".swiper-container").swiper({
                autoplay: 1000,
                //用户对其操作，是否停止autoplay
                autoplayDisableOnInteraction: true
            });
            //获取顶层列表
            var shopCategoryList = data.shopCategoryList;
            var categoryHtml = '';
            //遍历拼接前台展示
            shopCategoryList.map(function (item, index) {
                categoryHtml += ''
                             +  '<div class="col-50 shop-classify" data-category='+ item.shopCategoryId +'>'
                             +      '<div class="word">'
                             +          '<p class="shop-title">'+ item.shopCategoryName +'</p>'
                             +          '<p class="shop-desc">'+ item.shopCategoryDesc +'</p>'
                             +      '</div>'
                             +      '<div class="shop-classify-img-warp">'
                             +          '<img class="shop-img" src="'+ item.shopCategoryImg +'">'
                             +      '</div>'
                             +  '</div>';
            });
            $('.row').html(categoryHtml);
        }
    });
    
    //点击"我的",显示侧栏
    $('#me').click(function () {
        $.openPanel('#panel-left-demo');
    });

    $('.row').on('click', '.shop-classify', function (e) {
        var shopCategoryId = e.currentTarget.dataset.category;
        var newUrl = '/o2o/frontend/shoplist?parentId=' + shopCategoryId;
        window.location.href = newUrl;
    });

});
