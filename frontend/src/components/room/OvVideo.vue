<template>
	<video autoplay/>
</template>

<script>
export default {
	name: 'OvVideo',

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
  margin: 25px 30px 0;
	border-radius: 10px;
	/* line-height: 130px; */
}
</style>