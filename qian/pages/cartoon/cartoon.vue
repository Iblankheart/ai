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
			<view class="diygw-absolute autoview1_0">
				<image src="/static/160215.png" class="response diygw-col-24" mode="widthFix"></image>
			</view>
		</view>
		<view class="flex diygw-autoview diygw-col-24 flex-direction-column autoview-clz">
			<view class="diygw-absolute autoview_0">
				<view class="diygw-col-24 text-clz"> 人像动漫化 </view>
			</view>
		</view>
		
		
		
		
		<u-form :model="form" :rules="formRules" :errorType="['message', 'toast']" ref="formRef"
			class="flex diygw-form diygw-col-24">
			<u-form-item :borderBottom="false" :required="true" class="diygw-col-24" label="上传图片" labelPosition="top"
				prop="upload">
				<u-upload width="160" height="160" :before-upload="beforeUpload" action="http://localhost:8080/portrait"
					ref="uploadRef" :auto-upload="false" @upload-success="handleUploadSuccess"></u-upload>
			</u-form-item>
			<view @click="submitForm" class="diygw-col-24 bg-none text4-clz">提交</view>
		</u-form>
		
		
		
		<view class="diygw-col-24 text1-clz"> 结果： </view>
		<img class="imgm" :src="this.base64Image" v-if="base64Image"/>
		<view class="clearfix"></view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				datas:[],
				base64Image:"",
		
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

		},
		onReady() {
			this.$refs.formRef?.setRules(this.formRules);
		},
		
		
		methods: {
			submitForm() {
				return new Promise((resolve, reject) => {
					const uploadRef = this.$refs.uploadRef;
					uploadRef.upload({
						success: res => {
							console.log('上传成功，后端返回的数据:', res);
							this.handleUploadSuccess(
							res); // Call a method to handle the successful upload
							resolve(res); // Upload successful, call resolve
						},
						fail: err => {
							console.error('上传失败:', err);
							this.handleUploadFail(err); // Call a method to handle upload failure
							reject(err); // Upload failed, call reject
						}
					});
				});
			},
			handleUploadSuccess(data) {
				console.log('接收到的上传成功数据:', data);
				const responseData = JSON.parse(data);
				this.datas = responseData.image;
				this.base64Image = `data:image/jpeg;base64,${this.datas}`;
			
				console.log('保存数据:', this.datas);
			
				
			},
			
			
		}
		
	};
</script>

<style lang="scss" scoped>
	.imgm{
		width: 100%;
		height: 500px;
	}
	.bar1-clz {
		font-size: 26rpx !important;
	}
	.autoview1-clz {
		z-index: 1000;
		height: 440rpx;
	}

	.autoview1_0 {
		left: 88rpx;
		top: 0rpx;
		width: 586rpx;
		height: 440rpx;
		z-index: 0;
	}
	.autoview1_0 > .flex {
		height: 100%;
	}
	.autoview-clz {
		z-index: 1000;
		height: 120rpx;
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
		text-indent: 30rpx !important;
		font-size: 40rpx !important;
	}
	.container32674 {
		padding-left: 0px;
		padding-right: 0px;
	}
	.container32674 {
		padding-bottom: 80px;
	}
</style>
