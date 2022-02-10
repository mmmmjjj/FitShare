<template>
	<video autoplay/>
</template>

<script>
export default {
	name: 'OvSubscriberVideo',

	props: {
		streamManager: Object,
	},

	mounted () {
		this.streamManager.addVideoElement(this.$el);
	},
};
</script>

<style scoped>
video {
  height: 135px;
  margin: 20px 30px 0;
	border-radius: 10px;
	cursor: pointer;
	/* line-height: 130px; */
}
</style>