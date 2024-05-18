<template>
	<view class="container container32674">
		<view class="flex diygw-col-24 diygw-bottom bar1-clz">
			<view class="diygw-grid diygw-actions">
				<button @tap="navigateTo" data-type="page" data-url="/pages/img/img" class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/txsb.png"></image>
						</view>
						<view class="diygw-grid-title"> 图像识别 </view>
					</view>
				</button>
				<button @tap="navigateTo" data-type="page" data-url="/pages/text/text" class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/3wzsb.png"></image>
						</view>
						<view class="diygw-grid-title"> 文本识别 </view>
					</view>
				</button>
				<button @tap="navigateTo" data-type="page" data-url="/pages/nlp/nlp" class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/wbjc.png"></image>
						</view>
						<view class="diygw-grid-title"> 文本纠错 </view>
					</view>
				</button>
				<button @tap="navigateTo" data-type="page" data-url="/pages/cartoon/cartoon" class="diygw-action">
					<view class="diygw-grid-inner">
						<view class="diygw-grid-icon diygw-avatar" style="font-size: 56rpx">
							<image mode="aspectFit" class="diygw-avatar-img" src="/static/txdmh-copy.png"></image>
						</view>
						<view class="diygw-grid-title"> 人像动漫风 </view>
					</view>
				</button>
			</view>
		</view>
		<view class="flex diygw-autoview diygw-col-24 flex-direction-column autoview1-clz">
			<view >
				<image src="/static/designer.png" class="top" ></image>
			</view>
		</view>
		<view class="flex diygw-autoview diygw-col-24 flex-direction-column autoview-clz">
			<view class="diygw-absolute autoview_0">
				<view class="diygw-col-24 text-clz"> 文本纠错 </view>
			</view>
		</view>
		
		<u-form :model="form" :rules="formRules" :errorType="['message', 'toast']" ref="formRef" class="flex diygw-form diygw-col-24">
			<u-form-item class="diygw-col-24 textarea-clz" label="纠错文本" labelPosition="top" prop="textarea">
				<u-input maxlength="200" height="60px" class="" placeholder="请输入文本" v-model="form.textarea" type="textarea"></u-input>
			</u-form-item>
			<view @click="submitForm" class="diygw-col-24 bg-none text4-clz"> 提交 </view>
		</u-form>
		<view class="diygw-col-24 text1-clz"> 纠错后: </view>
		<view class="flex diygw-autoview diygw-col-24 flex-direction-column autoview2-clz">
			<view class="diygw-absolute autoview2_0">
				<view class="diygw-col-24 text2-clz">{{recognitionResult}}</view>
			</view>
		</view>
		<view class="clearfix"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				//用户全局信息
				userInfo: {},
				//页面传参
				globalOption: {},
				//自定义全局变量
				globalData: {},
				form: {
					textarea: ''
				},
				formRules: {},
				recognitionResult:"",
			};
		},
		onShow() {
			this.setCurrentPage(this);
		},
		onLoad(option) {
			this.setCurrentPage(this);
			if (option) {
				this.setData({
					globalOption: this.getOption(option)
				});
			}

			this.init();
		},
		onReady() {
			this.$refs.formRef?.setRules(this.formRules);
		},
		methods: {
			async init() {
				await this.initResetform();
			},
			initResetform() {
				this.initform = JSON.stringify(this.form);
			},
			resetForm() {
				this.form = JSON.parse(this.initform);
			},

			async submitForm() {
			    console.log('准备发送请求到后端');
			    // 构建请求参数，这里假设你的表单数据中有一个名为 'textarea' 的字段
			    const formData = new FormData();
			    formData.append('text', this.form.textarea);
			    
			    try {
			        // 发送 POST 请求到后端接口，替换成你的后端接口地址
			        let response = await fetch('http://localhost:8080/npl', {
			            method: 'POST',
			            body: formData 
			        });
			
			        // 解析后端返回的字符串数据
			        let data = await response.text();
			
			        // 在这里处理后端返回的数据，比如更新页面展示等
			        console.log('从后端获取的数据：', data);
					this.recognitionResult = data;
			        
			        // 如果需要对字符串数据进行进一步处理，可以在这里进行操作
			
			        // 显示成功提示
			        this.showToast('请求成功', 'success');
			    } catch (error) {
			        // 如果发生错误，显示错误信息
			        console.error('Error:', error);
			        this.showModal('网络请求出现问题，请重试', '提示', false);
			    }
			
			    return; // 添加 return 语句
			}

		}
	};
</script>

<style lang="scss" scoped>
	.top{
		width: 320px;
		height: 187.19px;
		margin-left: 36px;
	}
	.bar1-clz {
		font-size: 26rpx !important;
	}
	.autoview1-clz {
		z-index: 1000;
		height: 400rpx;
	}

	.autoview1_0 {
		left: 164rpx;
		top: 0rpx;
		width: 400rpx;
		height: 400rpx;
		z-index: 0;
	}
	.autoview1_0 > .flex {
		height: 100%;
	}
	.autoview-clz {
		z-index: 1000;
		height: 160rpx;
	}

	.autoview_0 {
		left: 126rpx;
		top: 16rpx;
		width: 456rpx;
		height: 118rpx;
		z-index: 0;
	}
	.autoview_0 > .flex {
		height: 100%;
	}
	.text-clz {
		font-size: 64rpx !important;
		text-align: center;
	}
	.textarea-clz {
		background-color: #f6f9ff;
		margin-left: 16rpx;
		border-bottom-left-radius: 12rpx;
		overflow: hidden;
		width: calc(100% - 16rpx - 16rpx) !important;
		border-top-left-radius: 12rpx;
		margin-top: 16rpx;
		border-top-right-radius: 12rpx;
		border-bottom-right-radius: 12rpx;
		margin-bottom: 16rpx;
		margin-right: 16rpx;
	}
	.text4-clz {
		padding-top: 20rpx;
		border-bottom-left-radius: 120rpx;
		color: #ffffff;
		font-weight: bold;
		letter-spacing: 6rpx !important;
		padding-left: 10rpx;
		font-size: 32rpx !important;
		padding-bottom: 20rpx;
		border-top-right-radius: 120rpx;
		margin-right: 30rpx;
		background-color: #2a928f;
		margin-left: 30rpx;
		box-shadow: 0rpx 6rpx 14rpx rgba(64, 195, 3, 0.35);
		overflow: hidden;
		width: calc(100% - 30rpx - 30rpx) !important;
		border-top-left-radius: 120rpx;
		margin-top: 10rpx;
		border-bottom-right-radius: 120rpx;
		margin-bottom: 10rpx;
		text-align: center;
		padding-right: 10rpx;
	}
	.text1-clz {
		font-size: 44rpx !important;
	}
	.autoview2-clz {
		z-index: 1000;
		height: 400rpx;
	}

	.autoview2_0 {
		left: 88rpx;
		top: 48rpx;
		width: 662rpx;
		height: 288rpx;
		z-index: 0;
	}
	.autoview2_0 > .flex {
		height: 100%;
	}
	.text2-clz {
		font-size: 36rpx !important;
	}
	.container32674 {
		padding-left: 0px;
		padding-right: 0px;
	}
	.container32674 {
		padding-bottom: 80px;
	}
</style>
